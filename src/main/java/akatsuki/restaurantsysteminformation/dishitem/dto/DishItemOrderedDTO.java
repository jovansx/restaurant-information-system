package akatsuki.restaurantsysteminformation.dishitem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishItemOrderedDTO {
    private String itemName;
    private int amount;
}
