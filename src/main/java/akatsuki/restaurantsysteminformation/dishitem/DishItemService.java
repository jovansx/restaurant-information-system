package akatsuki.restaurantsysteminformation.dishitem;

import java.util.List;

public interface DishItemService {
    DishItem create(DishItem dishItem);

    List<DishItem> getAllActive();

    DishItem getOne(long id);

    DishItem changeStateOfDishItems(long itemId, long userId);

    void delete(long id);
}
