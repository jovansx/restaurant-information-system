package akatsuki.restaurantsysteminformation.room.helper;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableRepresentationDTO;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List<RestaurantTableRepresentationDTO> getTablesDTO(List<RestaurantTable> roomTables) {
        List<RestaurantTableRepresentationDTO> tablesDTO = new ArrayList<>();
        roomTables.forEach(table -> {
            tablesDTO.add(new RestaurantTableRepresentationDTO(table));
        });
        return tablesDTO;
    }
}
