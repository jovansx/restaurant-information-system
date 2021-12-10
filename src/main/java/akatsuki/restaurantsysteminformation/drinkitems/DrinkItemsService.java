package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsUpdateDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface DrinkItemsService {

    DrinkItems getOne(long id);

    DrinkItems findOneWithItems(long id);

    List<DrinkItems> getAll();

    DrinkItems findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(long id);

    DrinkItems findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(long id);

    List<DrinkItems> findAllActiveAndFetchBartenderAndItems();

    DrinkItems create(DrinkItemsCreateDTO drinkItemsDTO);

    DrinkItems update(DrinkItemsUpdateDTO drinkItemsDTO, long id);

    DrinkItems changeStateOfDrinkItems(long itemId, long userId);

    DrinkItems delete(long id);

    void deleteById(long id);

    boolean isBartenderActive(UnregisteredUser user);

    void save(DrinkItems drinkItems);
}
