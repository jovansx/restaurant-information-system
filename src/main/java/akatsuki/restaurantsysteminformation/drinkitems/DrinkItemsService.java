package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface DrinkItemsService {

    DrinkItems findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(long id);

    List<DrinkItems> findAllActiveAndFetchBartenderAndItems();

    void create(DrinkItemsCreateDTO drinkItemsDTO);

    void update(DrinkItemsCreateDTO drinkItemsDTO, long id);

    void changeStateOfDrinkItems(long itemId, long userId);

    void delete(long id);

    boolean isBartenderActive(UnregisteredUser user);

}
