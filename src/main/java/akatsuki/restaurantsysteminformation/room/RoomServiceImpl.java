package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;

    @Autowired
    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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
        checkNameExistence(room.getName());
        roomRepository.save(room);
    }

    private void checkNameExistence(String name) {
        Optional<Room> room = roomRepository.findByName(name);
        if(room.isPresent()) {
            throw new RoomExistsException("Room with the name " + name + " already exists in the database.");
        }
    }
}
