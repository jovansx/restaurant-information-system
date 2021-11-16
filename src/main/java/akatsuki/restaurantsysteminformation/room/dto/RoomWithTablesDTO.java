package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class RoomWithTablesDTO extends RoomCreateDTO {
    private @Getter
    @Setter
    List<RestaurantTableDTO> tables;

    public RoomWithTablesDTO(String name, List<RestaurantTableDTO> tables) {
        super(name);
        this.tables = tables;
    }

    public RoomWithTablesDTO(Room room) {
        super(room.getName());
        this.tables = room.getRestaurantTables().stream().map(RestaurantTableDTO::new).collect(Collectors.toList());
    }
}
