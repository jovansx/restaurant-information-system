package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;

import java.util.List;

public class RoomWithTablesDTO extends RoomDTO {
    private List<RestaurantTableDTO> tables;

    public RoomWithTablesDTO(String name, List<RestaurantTableDTO> tables) {
        super(name);
        this.tables = tables;
    }

    public List<RestaurantTableDTO> getTables() {
        return tables;
    }

    public void setTables(List<RestaurantTableDTO> tableIds) {
        this.tables = tableIds;
    }
}
