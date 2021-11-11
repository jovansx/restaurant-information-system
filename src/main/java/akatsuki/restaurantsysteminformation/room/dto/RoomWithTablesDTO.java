package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableRepresentationDTO;

import java.util.List;

public class RoomWithTablesDTO extends RoomDTO {
    private List<RestaurantTableRepresentationDTO> tables;

    public RoomWithTablesDTO(String name, List<RestaurantTableRepresentationDTO> tables) {
        super(name);
        this.tables = tables;
    }

    public List<RestaurantTableRepresentationDTO> getTables() {
        return tables;
    }

    public void setTables(List<RestaurantTableRepresentationDTO> tableIds) {
        this.tables = tableIds;
    }
}
