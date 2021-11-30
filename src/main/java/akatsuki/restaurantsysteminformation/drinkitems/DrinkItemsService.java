package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface DrinkItemsService {

    DrinkItems findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(long id);

    DrinkItems findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(long id);

    List<DrinkItems> findAllActiveAndFetchBartenderAndItems();

    DrinkItems create(DrinkItemsCreateDTO drinkItemsDTO);

    DrinkItems update(DrinkItemsCreateDTO drinkItemsDTO, long id);

    DrinkItems changeStateOfDrinkItems(long itemId, long userId);

    DrinkItems delete(long id);

    boolean isBartenderActive(UnregisteredUser user);

}
