package akatsuki.restaurantsysteminformation.drinkitem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DrinkItemCreateDTO {
    private int amount;
    private Long itemId;
}
