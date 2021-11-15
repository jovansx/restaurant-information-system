package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface DrinkItemsService {

    DrinkItems getOne(long id);

    List<DrinkItems> getAll();

    DrinkItems getOneActive(long id);

    List<DrinkItems> getAllActive();

    DrinkItems changeStateOfDrinkItems(long itemId, long userId);

    void delete(long id);

    void create(DrinkItemsCreateDTO drinkItemsDTO);

    void update(DrinkItemsCreateDTO drinkItemsDTO, long id);

    boolean isBartenderActive(UnregisteredUser user);

}
