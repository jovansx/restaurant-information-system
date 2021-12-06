package akatsuki.restaurantsysteminformation.restauranttable.dto;

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
    private String state;
    private String shape;
    @Min(0)
    private int row;
    @Min(0)
    private int column;
}
