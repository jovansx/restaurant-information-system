package akatsuki.restaurantsysteminformation.dishitem;

import java.util.List;

public interface DishItemService {

    List<DishItem> getAllActive();

    DishItem getOne(long id);

    DishItem changeStateOfDishItems(long itemId, long userId);
}
