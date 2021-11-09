package akatsuki.restaurantsysteminformation.room.mapper;

import akatsuki.restaurantsysteminformation.room.Room;
import akatsuki.restaurantsysteminformation.room.dto.CreateRoomDTO;

import java.util.ArrayList;

public class Mapper {

    public static Room convertCreateRoomDTOToRoom(CreateRoomDTO createRoomDTO) {
        return new Room(createRoomDTO.getName(), false, new ArrayList<>());
    }
}
