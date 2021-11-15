package akatsuki.restaurantsysteminformation.restauranttable.dto;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableDTO extends RestaurantTableCreateDTO {
    private long id;

    public RestaurantTableDTO(RestaurantTable table) {
        super(table.getName(), table.getState().toString(), table.getShape().toString());
        this.id = table.getId();
    }
}
