package akatsuki.restaurantsysteminformation.drinkitem.dto;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DrinkItemDTO {
    private long id;
    private int amount;
    private String itemName;

    public DrinkItemDTO(DrinkItem drinkItem) {
        this.id = drinkItem.getId();
        this.amount = drinkItem.getAmount();
        this.itemName = drinkItem.getItem().getName();
    }
}
