package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface DishItemService {

    DishItem getOne(long id);

    List<DishItem> getAll();

    DishItem findOneActiveAndFetchItemAndChef(long id);

    List<DishItem> findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();

    DishItem create(DishItemCreateDTO dishItem);

    DishItem update(DishItemCreateDTO dishItemCreateDTO, long id);

    DishItem changeStateOfDishItems(long itemId, long userId);

    DishItem delete(long id);

    void deleteById(long id);

    boolean isChefActive(UnregisteredUser user);

    void save(DishItem dishItem);
}
