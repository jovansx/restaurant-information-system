package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;

import java.util.List;

public interface RoomService {
    Room getOne(long id);

    List<Room> getAll();

    void create(Room room);

    void update(Room room, long id);

    void delete(long id);

    void updateByRoomDTO(RoomUpdateDTO roomDTO, long id);

}
