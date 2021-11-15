package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateDTO {
    @NotNull(message = "It cannot be null.")
    List<RestaurantTableCreateDTO> newTables;

    @NotNull(message = "It cannot be null.")
    List<RestaurantTableDTO> updateTables;

    @NotNull(message = "It cannot be null.")
    List<Long> deleteTables;

    @NotEmpty(message = "It cannot be empty.")
    private String name;
}
