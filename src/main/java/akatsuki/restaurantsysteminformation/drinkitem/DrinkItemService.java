package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;

public interface DrinkItemService {

    DrinkItem create(DrinkItemUpdateDTO drinkItem);

    DrinkItem update(DrinkItemUpdateDTO drinkItem, long id);

    void delete(long id);

    DrinkItem findByIdAndFetchItem(long id);

    DrinkItem getOne(long id);
}
