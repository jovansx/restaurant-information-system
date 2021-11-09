package akatsuki.restaurantsysteminformation.restauranttable.mapper;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;

public class Mapper {
    public static RestaurantTable convertCreateRestaurantTableDTOToRestaurantTable(CreateRestaurantTableDTO createRestaurantTableDTO) {
        return new RestaurantTable(
            createRestaurantTableDTO.getName(),
            TableState.valueOf(createRestaurantTableDTO.getState()),
            TableShape.valueOf(createRestaurantTableDTO.getShape()),
            false,
            null
        );
    }
}
