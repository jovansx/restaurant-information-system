package akatsuki.restaurantsysteminformation.restauranttable.dto;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableCreateDTO {
    private String name;
    private TableState state;
    private TableShape shape;
    @Min(0)
    private int row;
    @Min(0)
    private int column;
}
