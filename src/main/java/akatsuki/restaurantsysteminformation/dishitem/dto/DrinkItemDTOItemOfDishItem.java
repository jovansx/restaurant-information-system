package akatsuki.restaurantsysteminformation.dishitem.dto;

import akatsuki.restaurantsysteminformation.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DrinkItemDTOItemOfDishItem {
    private String itemName;
    private int amount;

    public DrinkItemDTOItemOfDishItem(Item item, int amount) {
        this.itemName = item.getName();
        this.amount = amount;
    }
}
