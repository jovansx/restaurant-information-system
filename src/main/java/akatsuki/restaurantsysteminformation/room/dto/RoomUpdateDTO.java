package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateDTO {
    List<RestaurantTableCreateDTO> newTables;
    List<RestaurantTableDTO> updateTables;
    List<Long> deleteTables;
    private String name;
}
