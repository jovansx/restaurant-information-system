package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface DishItemService {

    DishItem findOneActiveAndFetchItemAndChef(long id);

    List<DishItem> findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();

    void create(DishItemCreateDTO dishItem);

    void update(DishItemCreateDTO dishItemCreateDTO, long id);

    DishItem changeStateOfDishItems(long itemId, long userId);

    void delete(long id);

    boolean isChefActive(UnregisteredUser user);
}
