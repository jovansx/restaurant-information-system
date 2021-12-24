package akatsuki.restaurantsysteminformation.drinkitem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrinkItemCreateDTO {
    private int amount;
    private long itemId;
}
