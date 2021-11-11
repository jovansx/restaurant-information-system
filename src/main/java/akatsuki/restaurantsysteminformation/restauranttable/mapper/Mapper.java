package akatsuki.restaurantsysteminformation.restauranttable.mapper;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.UpdateRestaurantTableDTO;

public class Mapper {
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

    public static RestaurantTable convertCreateRestaurantTableDTOToRestaurantTable(UpdateRestaurantTableDTO restaurantTableDTO) {
        // TODO add check
        return new RestaurantTable(
                restaurantTableDTO.getName(),
                TableState.valueOf(restaurantTableDTO.getState()),
                TableShape.valueOf(restaurantTableDTO.getShape()),
                false,
                null
        );
    }
}
