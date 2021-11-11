package akatsuki.restaurantsysteminformation.room.mapper;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.UpdateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.Room;
import akatsuki.restaurantsysteminformation.room.dto.RoomDTO;

import java.util.ArrayList;

public class Mapper {

    public static Room convertRoomDTOToRoom(RoomDTO roomDTO) {
        return new Room(roomDTO.getName(), false, new ArrayList<>());
    }

    public static RestaurantTable convertCreateRestaurantTableDTOToRestaurantTable(CreateRestaurantTableDTO createRestaurantTableDTO) {
        // TODO add check
        return new RestaurantTable(
                createRestaurantTableDTO.getName(),
                TableState.valueOf(createRestaurantTableDTO.getState()),
                TableShape.valueOf(createRestaurantTableDTO.getShape()),
                false,
                null
        );
    }

    public static RestaurantTable convertCreateRestaurantTableDTOToRestaurantTable(UpdateRestaurantTableDTO updateRestaurantTableDTO) {
        // TODO add check
        return new RestaurantTable(
                updateRestaurantTableDTO.getName(),
                TableState.valueOf(updateRestaurantTableDTO.getState()),
                TableShape.valueOf(updateRestaurantTableDTO.getShape()),
                false,
                null
        );
    }
}
