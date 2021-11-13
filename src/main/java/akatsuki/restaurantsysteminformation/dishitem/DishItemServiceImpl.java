package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.exception.UserDeletedException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishItemServiceImpl implements DishItemService {
    private DishItemRepository dishItemRepository;
    private UnregisteredUserService unregisteredUserService;

    @Autowired
    public void setDishItemRepository(DishItemRepository dishItemRepository, UnregisteredUserService unregisteredUserService) {
        this.dishItemRepository = dishItemRepository;
        this.unregisteredUserService = unregisteredUserService;
    }

    @Override
    public List<DishItem> getAllActive() {
        List<DishItem> dishItemsList = dishItemRepository.findAllNotOnHoldActive();
        dishItemsList.addAll(this.dishItemRepository.findAllOnHoldActive());
        return dishItemsList;
    }

    @Override
    public DishItem changeStateOfDishItems(long itemId, long userId) {
        DishItem dishItem = this.dishItemRepository.findById(itemId).orElseThrow(
                () -> new DrinkItemsNotFoundException("Dish item with the id " + itemId + " is not found in the database.")
        );
        UserType typeOfAllowedUser;
        if (dishItem.getState().equals(ItemState.READY))
            typeOfAllowedUser = UserType.WAITER;
        else if (dishItem.getState().equals(ItemState.ON_HOLD) || dishItem.getState().equals(ItemState.PREPARATION))
            typeOfAllowedUser = UserType.CHEF;
        else
            throw new DrinkItemsNotFoundException("Dish item with state of  " + dishItem.getState().name() + " is not valid for changing states.");

        UnregisteredUser bartender = this.unregisteredUserService.getOne(userId);
        if (!bartender.getType().equals(typeOfAllowedUser)) {
            throw new UserNotFoundException("User with the id " + userId + " is not a bartender.");
        }

        if (dishItem.getState().equals(ItemState.ON_HOLD)) {
            dishItem.setState(ItemState.PREPARATION);
            dishItem.setChef(bartender);
        } else if (dishItem.getState().equals(ItemState.PREPARATION)) {
            dishItem.setState(ItemState.READY);
        } else {
            dishItem.setState(ItemState.DELIVERED);
        }
        dishItemRepository.save(dishItem);
        return dishItem;
    }

    @Override
    public DishItem getOne(long id) {
        return this.dishItemRepository.findOneActiveWithChef(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public DishItem create(DishItem dishItem) {
        return dishItemRepository.save(dishItem);
    }

    @Override
    public void delete(long id) {
        Optional<DishItem> dishItemOptional = dishItemRepository.findById(id);
        if (dishItemOptional.isEmpty()) {
            throw new DrinkItemsNotFoundException("Dish item with the id " + id + " is not found in the database.");
        }

        DishItem dishItem = dishItemOptional.get();

        if (dishItem.isDeleted()) {
            throw new UserDeletedException("Dish item with the id " + id + " is  already deleted.");
        }

        dishItem.setDeleted(true);
//        TODO razmisli o ovome
        dishItem.setActive(false);
        dishItemRepository.save(dishItem);
    }

}
