package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;

import java.util.Collection;
import java.util.List;

public interface DishItemService {

    List<DishItem> getAllActive();

    DishItem getOne(long id);

    DishItem changeStateOfDishItems(long itemId, long userId);
}
