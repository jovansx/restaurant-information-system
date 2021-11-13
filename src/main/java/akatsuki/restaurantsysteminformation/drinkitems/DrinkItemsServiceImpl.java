package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrinkItemsServiceImpl implements DrinkItemsService {
    private DrinkItemsRepository drinkItemsRepository;

    private UnregisteredUserService unregisteredUserService;

    //TODO CRUD
    @Autowired
    public void setDrinkItemsRepository(DrinkItemsRepository drinkItemsRepository, UnregisteredUserService unregisteredUserService) {
        this.drinkItemsRepository = drinkItemsRepository;
        this.unregisteredUserService = unregisteredUserService;
    }

    @Override
    public List<DrinkItems> getAllActive() {
        List<DrinkItems> drinkItemsList = drinkItemsRepository.findAllNotOnHoldActive();
        drinkItemsList.addAll(this.drinkItemsRepository.findAllOnHoldActive());
        return drinkItemsList;
    }

    @Override
    public DrinkItems getOne(long id) {
        return this.drinkItemsRepository.findOneActiveWithBartenderAndWithItems(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + id + " are not found in the database.")
        );
    }

    @Override
    public DrinkItems changeStateOfDrinkItems(long itemId, long userId) {
        DrinkItems drinkItems = this.drinkItemsRepository.findById(itemId).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + itemId + " are not found in the database.")
        );
        UserType typeOfAllowedUser;
        if (drinkItems.getState().equals(ItemState.READY))
            typeOfAllowedUser = UserType.WAITER;
        else if (drinkItems.getState().equals(ItemState.ON_HOLD) || drinkItems.getState().equals(ItemState.PREPARATION))
            typeOfAllowedUser = UserType.BARTENDER;
        else
            throw new DrinkItemsNotFoundException("Drink items with state of  " + drinkItems.getState().name() + " are not valid for changing states.");


        UnregisteredUser bartender = this.unregisteredUserService.getOne(userId);
        if (!bartender.getType().equals(typeOfAllowedUser)) {
            throw new UserNotFoundException("User with the id " + userId + " is not a bartender.");
        }

        if (drinkItems.getState().equals(ItemState.ON_HOLD)) {
            drinkItems.setState(ItemState.PREPARATION);
            drinkItems.setBartender(bartender);
        } else if (drinkItems.getState().equals(ItemState.PREPARATION)) {
            drinkItems.setState(ItemState.READY);
        } else {
            drinkItems.setState(ItemState.DELIVERED);
        }
        drinkItemsRepository.save(drinkItems);
        return drinkItems;
    }

    @Override
    public DrinkItems create(DrinkItems drinkItems) {
        return drinkItemsRepository.save(drinkItems);
    }
}
