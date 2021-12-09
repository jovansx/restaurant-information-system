package akatsuki.restaurantsysteminformation.restauranttable.dto;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
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
        super(table.getName(), table.getState(), table.getShape(), table.getRow(), table.getColumn());
        this.id = table.getId();
    }

    public RestaurantTableDTO(String name, TableState state, TableShape shape, long id, int row, int column) {
        super(name, state, shape, row, column);
        this.id = id;
    }
}
