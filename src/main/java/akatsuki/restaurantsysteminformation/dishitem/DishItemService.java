package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface DishItemService {
    void create(DishItemCreateDTO dishItem);

    List<DishItem> getAllActive();

    DishItem getOneWithChef(long id);

    DishItem getOne(long id);

    DishItem getOneActive(long id);

    DishItem changeStateOfDishItems(long itemId, long userId);

    void delete(long id);

    void update(DishItemCreateDTO dishItemCreateDTO, long id);

    void prepare(long id, long waiterId);

    boolean isChefActive(UnregisteredUser user);
}
