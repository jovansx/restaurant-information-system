package akatsuki.restaurantsysteminformation.room.mapper;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.room.Room;
import akatsuki.restaurantsysteminformation.room.dto.CreateRoomDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomDTO;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static Room convertCreateRoomDTOToRoom(CreateRoomDTO createRoomDTO) {
        return new Room(createRoomDTO.getName(), false, new ArrayList<>());
    }

    public static Room convertUpdateRoomDTOToRoom(RoomDTO updateRoomDTO, List<RestaurantTable> tables) {
        return new Room(updateRoomDTO.getName(), false, tables);
    }
}
