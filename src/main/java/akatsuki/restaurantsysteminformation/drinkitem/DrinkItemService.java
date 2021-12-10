package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;

public interface DrinkItemService {

    DrinkItem create(DrinkItemUpdateDTO drinkItem);

    DrinkItem update(DrinkItemUpdateDTO drinkItem, long id);

    DrinkItem delete(DrinkItem drinkItem);

    void delete(long id);

    DrinkItem findByIdAndFetchItem(long id);
    DrinkItem getOne(long id);
}
