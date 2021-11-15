package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.room.dto.UpdateRoomDTO;

import java.util.List;

public interface RoomService {
    List<Room> getAll();

    Room getOne(long id);

    void create(Room room);

//    void update(Room room, long id);
    void update(Room room, long id);

    void delete(long id);

    List<RestaurantTable> getRoomTables(long id);

    void checkTableInRoom(long tableId, long id);
    void updateByRoomDTO(UpdateRoomDTO roomDTO, long id);

}
