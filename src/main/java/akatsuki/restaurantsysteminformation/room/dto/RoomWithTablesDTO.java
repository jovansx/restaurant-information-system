package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class RoomWithTablesDTO extends RoomCreateDTO {
    private @Getter @Setter List<RestaurantTableDTO> tables;

    public RoomWithTablesDTO(String name, List<RestaurantTableDTO> tables) {
        super(name);
        this.tables = tables;
    }
}
