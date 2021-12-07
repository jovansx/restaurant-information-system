package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomTablesUpdateDTO {

    @NotNull(message = "It cannot be null.")
    List<RestaurantTableDTO> tables;
}
