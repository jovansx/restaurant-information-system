package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableRepresentationDTO;
import akatsuki.restaurantsysteminformation.room.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomWithTablesDTO extends RoomDTO {
    private List<RestaurantTableRepresentationDTO> tables;

    public RoomWithTablesDTO(Room room) {
        super(room.getName());
        this.tables = new ArrayList<>();
        for (RestaurantTable table : room.getRestaurantTables()) {
            tables.add(new RestaurantTableRepresentationDTO(table));
        }
    }

    public List<RestaurantTableRepresentationDTO> getTables() {
        return tables;
    }

    public void setTables(List<RestaurantTableRepresentationDTO> tableIds) {
        this.tables = tableIds;
    }
}
