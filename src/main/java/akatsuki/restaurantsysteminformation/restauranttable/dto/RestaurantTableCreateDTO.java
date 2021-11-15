package akatsuki.restaurantsysteminformation.restauranttable.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableCreateDTO {
    private String name;
    private String state;
    private String shape;
}
