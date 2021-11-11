package akatsuki.restaurantsysteminformation.drinkitems.mapper;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.enums.ItemState;

import java.time.LocalDateTime;
import java.util.List;

public class DrinkItemsMapper {
    public static DrinkItems convertDishItemCreateDTOToDishItem(DrinkItemsCreateDTO drinkItemsCreateDTO, LocalDateTime dateTime, List<DrinkItem> drinkItemList) {
        return new DrinkItems(drinkItemsCreateDTO.getNotes(), dateTime, false, ItemState.NEW, null, drinkItemList, true);
    }
}
