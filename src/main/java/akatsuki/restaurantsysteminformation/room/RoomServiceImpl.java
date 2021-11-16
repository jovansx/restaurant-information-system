package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletionFailedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RestaurantTableService restaurantTableService;

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

    @Override
    public void update(Room room, long id) {
        Room foundRoom = getOne(id);

        foundRoom.setName(room.getName());
        foundRoom.setRestaurantTables(room.getRestaurantTables());

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

    private List<RestaurantTable> createNewTables(List<RestaurantTableCreateDTO> newTablesDTO) {
        List<RestaurantTable> tables = new ArrayList<>();
        newTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = restaurantTableService.create(new RestaurantTable(tableDTO));
            tables.add(table);
        });
        return tables;
    }

    private List<RestaurantTable> updateTables(List<RestaurantTableDTO> updateTablesDTO, long id) {
        List<RestaurantTable> tables = new ArrayList<>();
        updateTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = new RestaurantTable(tableDTO);
            checkTableInRoom(tableDTO.getId(), id);
            tables.add(restaurantTableService.update(table, tableDTO.getId()));
        });
        return tables;
    }

    private void deleteTables(List<Long> deleteTableIds, long id) {
        deleteTableIds.forEach(tableId -> {
            checkTableInRoom(tableId, id);
            restaurantTableService.delete(tableId);
        });
    }

    @Override
    public void updateByRoomDTO(RoomUpdateDTO roomDTO, long id) {
        checkNameExistence(roomDTO.getName(), id);

        List<RestaurantTable> newTables = createNewTables(roomDTO.getNewTables());
        List<RestaurantTable> updatedTables = updateTables(roomDTO.getUpdateTables(), id);
        deleteTables(roomDTO.getDeleteTables(), id);

        List<RestaurantTable> allTables = new ArrayList<>(updatedTables);
        List<RestaurantTable> roomTables = getRoomTables(id);
        roomTables.forEach(table -> {
            if (!allTables.contains(table)) {
                allTables.add(table);
            }
        });
        allTables.addAll(newTables);

        Room room = new Room(roomDTO.getName(), false, allTables);
        update(room, id);
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
