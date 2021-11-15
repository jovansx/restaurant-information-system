package akatsuki.restaurantsysteminformation.room.helper;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<RestaurantTableDTO> getTablesDTO(List<RestaurantTable> roomTables) {
        List<RestaurantTableDTO> tablesDTO = new ArrayList<>();
        roomTables.forEach(table -> {
            tablesDTO.add(new RestaurantTableDTO(table));
        });
        return tablesDTO;
    }
}
