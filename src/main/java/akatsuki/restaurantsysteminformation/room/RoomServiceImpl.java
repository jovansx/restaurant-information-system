package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.UpdateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.dto.UpdateRoomDTO;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletionFailedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RestaurantTableService restaurantTableService;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RestaurantTableService restaurantTableService) {
        this.roomRepository = roomRepository;
        this.restaurantTableService = restaurantTableService;
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room getOne(long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new RoomNotFoundException("Room the id " + id + " is not found in the database.")
        );
    }

    @Override
    public void create(Room room) {
        checkNameExistence(room.getName(), -1);
        roomRepository.save(room);
    }

//    @Override
//    public void update(Room room, long id) {
//        Room foundRoom = getOne(id);
//        checkNameExistence(room.getName(), id);
//
//        foundRoom.setName(room.getName());
//        foundRoom.setRestaurantTables(room.getRestaurantTables());
//
//        roomRepository.save(foundRoom);
//    }

//    TODO Jelena aj proveri jel radi ovo
    @Transactional
    @Override
    public void update(UpdateRoomDTO updateRoomDTO, long id) {
        Room foundRoom = getOne(id);
        checkNameExistence(updateRoomDTO.getName(), id);

        List<RestaurantTable> allTables = new ArrayList<>();

        List<CreateRestaurantTableDTO> newTablesDTO = updateRoomDTO.getNewTables();

        newTablesDTO.forEach(tableDTO -> allTables.add(restaurantTableService.create(new RestaurantTable(tableDTO))));

        List<UpdateRestaurantTableDTO> updateTablesDTO = updateRoomDTO.getUpdateTables();
        updateTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = new RestaurantTable(tableDTO);
            checkTableInRoom(tableDTO.getId(), id);
            allTables.add(restaurantTableService.update(table, tableDTO.getId()));
        });

        List<Long> deleteTableIds = updateRoomDTO.getDeleteTables();
        deleteTableIds.forEach(tableId -> {
            checkTableInRoom(tableId, id);
            restaurantTableService.delete(tableId);
        });

        List<RestaurantTable> roomTables = getRoomTables(id);
        roomTables.forEach(table -> {
            if (!allTables.contains(table)) {
                allTables.add(table);
            }
        });

        foundRoom.setName(updateRoomDTO.getName());
        foundRoom.setRestaurantTables(allTables);

        roomRepository.save(foundRoom);
    }

    @Override
    public void delete(long id) {
        Room room = getOne(id);
        room.setDeleted(true);
        room.getRestaurantTables().forEach(table -> {
            if (table.getActiveOrder() != null) {
                throw new RoomDeletionFailedException("Room with the id " + id + " cannot be deleted because it has active order.");
            }
            restaurantTableService.delete(table.getId());
        });
        roomRepository.save(room);
    }

    @Override
    public List<RestaurantTable> getRoomTables(long id) {
        Room room = getOne(id);
        return room.getRestaurantTables();
    }

    @Override
    public void checkTableInRoom(long tableId, long id) {
        Room room = getOne(id);
        RestaurantTable table = restaurantTableService.getOne(tableId);
        if (!room.getRestaurantTables().contains(table)) {
            throw new RestaurantTableNotAvailableException("Restaurant table with the id " + table.getId() + " is not available in the room " + room.getName());
        }
    }

    private void checkNameExistence(String name, long id) {
        Optional<Room> room = roomRepository.findByName(name);
        if (id == -1 && room.isPresent()) {
            throw new RoomExistsException("Room with the name " + name + " already exists in the database.");
        }

        if (room.isPresent() && room.get().getId() != id) {
            throw new RoomExistsException("Room with the name " + name + " already exists in the database.");
        }
    }

}
