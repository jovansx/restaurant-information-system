package akatsuki.restaurantsysteminformation.drinkitems;

import java.util.List;

public interface DrinkItemsService {

    List<DrinkItems> getAllActive();

    DrinkItems getOne(long id);

    DrinkItems changeStateOfDrinkItems(long itemId, long userId);

    DrinkItems create(DrinkItems drinkItems);
}
