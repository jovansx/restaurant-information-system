package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DrinkItemsServiceImpl implements DrinkItemsService {
    private DrinkItemsRepository drinkItemsRepository;

    @Autowired
    public void setDrinkItemsRepository(DrinkItemsRepository drinkItemsRepository) {
        this.drinkItemsRepository = drinkItemsRepository;
    }

    @Override
    public List<DrinkItems> getAllActive() {
        List<DrinkItems> drinkItemsList = drinkItemsRepository.findAllNotOnHoldActive();
        drinkItemsList.addAll(this.drinkItemsRepository.findAllOnHoldActive());
        return drinkItemsList;
    }

    //TODO vidi kako drugacije
    @Override
    public DrinkItems getOne(long id) {
        DrinkItems drinkItems = this.drinkItemsRepository.findOneActive(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + id + " are not found in the database.")
        );
        if (!drinkItems.getState().equals(ItemState.ON_HOLD)){
            drinkItems = this.drinkItemsRepository.findOneActiveWithBartender(id).get();
        }
        return drinkItems;
    }
}
