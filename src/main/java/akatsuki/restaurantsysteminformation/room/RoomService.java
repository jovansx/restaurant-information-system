package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;

import java.util.List;

public interface RoomService {
    Room getOne(long id);

    List<Room> getAll();

    Room create(Room room);

    Room update(Room room, long id);

    Room delete(long id);

    Room updateByRoomDTO(RoomUpdateDTO roomDTO, long id);

    void save(Room room);
}
