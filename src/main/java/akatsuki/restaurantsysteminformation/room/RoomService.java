package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;

import java.util.List;

public interface RoomService {
    List<Room> getAll();
    Room getOne(long id);
    void create(Room room);
    void update(Room room, List<RestaurantTable> tables, long id);
    void delete(long id);
    List<Long> getRoomTableIds(long roomId);
}
