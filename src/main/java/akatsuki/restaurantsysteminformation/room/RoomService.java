package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;

import java.util.List;

public interface RoomService {
    List<Room> getAll();
    Room getOne(long id);
    void create(Room room);
    void update(Room room, long id);
    void delete(long id);
    List<Long> getRoomTableIds(long roomId);
    List<RestaurantTable> getRoomTables(long id);
    void checkTablesAvailability(List<RestaurantTable> tables, long roomId);

    void addTableToRoom(RestaurantTable table, long roomId);

    void updateRoomTable(RestaurantTable updatedTable, long roomId);

    void checkTableInRoom(long tableId, long id);
}
