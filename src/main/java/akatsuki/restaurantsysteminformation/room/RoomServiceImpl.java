package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.exception.UserDeletedException;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    private RestaurantTableService restaurantTableService;

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

    @Override
    public void update(Room room, List<RestaurantTable> tables, long id) {
        Optional<Room> roomMaybe = roomRepository.findById(id);
        if(roomMaybe.isEmpty()) {
            throw new RoomNotFoundException("Room the id " + id + " is not found in the database.");
        } else if (roomMaybe.get().isDeleted()) {
            throw new RoomExistsException("Room with the id " + id + " is deleted.");
        }
        checkNameExistence(room.getName(), id);
        checkTablesAvailability(tables, id);

        Room foundRoom = roomMaybe.get();
        foundRoom.setName(room.getName());
        foundRoom.setRestaurantTables(tables);

        roomRepository.save(foundRoom);
    }

    @Override
    public void delete(long id) {
        Room room = deleteValidation(id);
        room.setDeleted(true);
        roomRepository.save(room);
    }

    @Override
    public List<Long> getRoomTableIds(long roomId) {
        Room room = roomRepository.findById(roomId).get();
        List<RestaurantTable> tables = room.getRestaurantTables();
        List<Long> tableIds = new ArrayList<>();
        tables.forEach(table -> {
            tableIds.add(table.getId());
        });
        return tableIds;
    }


    private void checkTablesAvailability(List<RestaurantTable> tables, long roomId) {
        List<Room> allRooms = getAll();
        allRooms.forEach(room -> {
            List<RestaurantTable> allTables = room.getRestaurantTables();
            allTables.forEach(table -> {
                if(tables.contains(table) && room.getId() != roomId) {
                    throw new RestaurantTableNotAvailableException("Restaurant table with the id " + table.getId() + " is already taken.");
                }
            });
        });
    }

    private Room deleteValidation(long id) {
        Optional<Room> roomMaybe = roomRepository.findById(id);
        if (roomMaybe.isEmpty()) {
            throw new RoomNotFoundException("Room with the id " + id + " is not found in the database.");
        }
        if (roomMaybe.get().isDeleted()) {
            throw new RoomDeletedException("Room with the id " + id + " already deleted.");
        }
        return roomMaybe.get();
    }

    private void checkNameExistence(String name, long id) {
        if(id == -1) return;        Optional<Room> room = roomRepository.findByName(name);
        if(room.isPresent() && room.get().getId() != id) {
            throw new RoomExistsException("Room with the name " + name + " already exists in the database.");
        }
    }
}
