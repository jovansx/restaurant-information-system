package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;

import java.util.List;

public interface RoomService {
    List<Room> getAll();

    Room getOne(long id);

    void create(Room room);

    void update(Room room, long id);

    void delete(long id);

    List<RestaurantTable> getRoomTables(long id);

    void checkTableInRoom(long tableId, long id);

    void updateByRoomDTO(RoomUpdateDTO roomDTO, long id);

}
