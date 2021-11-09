package akatsuki.restaurantsysteminformation.room;

import java.util.List;

public interface RoomService {
    List<Room> getAll();
    Room getOne(long id);
    void create(Room room);
}
