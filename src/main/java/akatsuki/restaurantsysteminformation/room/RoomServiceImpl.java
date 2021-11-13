package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletionFailedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void update(Room room, long id) {
        Room foundRoom = checkRoomExistence(id);
        checkNameExistence(room.getName(), id);

        foundRoom.setName(room.getName());
        foundRoom.setRestaurantTables(room.getRestaurantTables());

        roomRepository.save(foundRoom);
    }

    @Override
    public void delete(long id) {
        Room room = deleteValidation(id);
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
        Room room = checkRoomExistence(id);
        return room.getRestaurantTables();
    }

    @Override
    public void checkTableInRoom(long tableId, long id) {
        Room room = checkRoomExistence(id);
        RestaurantTable table = restaurantTableService.getOne(tableId);
        if (!room.getRestaurantTables().contains(table)) {
            throw new RestaurantTableNotAvailableException("Restaurant table with the id " + table.getId() + " is not available in the room " + room.getName());
        }
    }

    private Room deleteValidation(long id) {
        return checkRoomExistence(id);
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

    private Room checkRoomExistence(long id) {
        Optional<Room> roomMaybe = roomRepository.findById(id);
        if (roomMaybe.isEmpty()) {
            throw new RoomNotFoundException("Room with the id " + id + " is not found in the database.");
        }
        return roomMaybe.get();
    }
}
