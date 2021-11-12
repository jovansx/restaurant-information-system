package akatsuki.restaurantsysteminformation.drinkitem.mapper;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.item.Item;

public class DrinkItemMapper {

    public static DrinkItem convertDrinkItemCreateDTOToDrinkItem(int amount, Item item) {
        return new DrinkItem(amount, item);
    }
}
