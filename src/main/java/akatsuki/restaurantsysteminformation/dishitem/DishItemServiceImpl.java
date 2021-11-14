package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidStateException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidTypeException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemOrderException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishItemServiceImpl implements DishItemService {
    private DishItemRepository dishItemRepository;
    private UnregisteredUserService unregisteredUserService;
    private OrderService orderService;
    private ItemService itemService;

    @Autowired
    public void setDishItemRepository(DishItemRepository dishItemRepository, UnregisteredUserService unregisteredUserService, OrderService orderService, ItemService itemService) {
        this.dishItemRepository = dishItemRepository;
        this.unregisteredUserService = unregisteredUserService;
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @Override
    public DishItem getOne(long id) {
        return dishItemRepository.findById(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<DishItem> getAllActive() {
        List<DishItem> dishItemsList = dishItemRepository.findAllNotOnHoldActive();
        dishItemsList.addAll(this.dishItemRepository.findAllOnHoldActive());
        return dishItemsList;
    }

    @Override
    public DishItem changeStateOfDishItems(long itemId, long userId) {
        DishItem dishItem = getOne(itemId);
        UserType typeOfAllowedUser;
        if (dishItem.getState().equals(ItemState.READY))
            typeOfAllowedUser = UserType.WAITER;
        else if (dishItem.getState().equals(ItemState.ON_HOLD) || dishItem.getState().equals(ItemState.PREPARATION))
            typeOfAllowedUser = UserType.CHEF;
        else
            throw new DishItemNotFoundException("Dish item with state of  " + dishItem.getState().name() + " is not valid for changing states.");

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
    public DishItem getOneWithChef(long id) {
        return this.dishItemRepository.findOneActiveWithChef(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public void create(DishItemCreateDTO itemCreateDTO) {
        Order order = orderService.getOne(itemCreateDTO.getOrderId());
        Item item = itemService.getOne(itemCreateDTO.getItemId());
        if (item.getType().equals(ItemType.DISH)) {
            throw new DishItemInvalidTypeException("Item type is not DISH.");
        }
        DishItem dishItem = new DishItem(itemCreateDTO.getNotes(), LocalDateTime.now(), false, ItemState.NEW, itemCreateDTO.getAmount(), null, item, true);
        dishItem = dishItemRepository.save(dishItem);
        orderService.addDishItemToOrder(dishItem, order);
    }

    @Override
    public void update(DishItemCreateDTO itemCreateDTO, long id) {
        Order order = orderService.getOne(itemCreateDTO.getOrderId());
        Item item = itemService.getOne(itemCreateDTO.getItemId());
        if (item.getType().equals(ItemType.DISH)) {
            throw new DishItemInvalidTypeException("Item type is not DISH.");
        }
        DishItem dishItem = getOne(id);
        boolean dishIsInOrder = false;
        for (DishItem di : order.getDishes()) {
            if (di.getId().equals(dishItem.getId())) {
                dishIsInOrder = true;
                break;
            }
        }
        if (!dishIsInOrder) {
            throw new DishItemOrderException("Dish item order id is not equal to " + itemCreateDTO.getOrderId() + ". Order cannot be changed.");
        }

        ItemState itemState = dishItem.getState();
        if (!itemState.equals(ItemState.NEW) && !itemState.equals(ItemState.ON_HOLD)) {
            throw new DishItemInvalidStateException("Cannot change dish item, because its state is " + itemState.name().toLowerCase() + ".");
        }

        dishItem.setItem(item);
        dishItem.setAmount(itemCreateDTO.getAmount());
        dishItem.setNotes(itemCreateDTO.getNotes());
        dishItemRepository.save(dishItem);
    }

    @Override
    public void delete(long id) {
        DishItem dishItem = getOne(id);
        dishItem.setDeleted(true);
        dishItem.setActive(false);
        dishItemRepository.save(dishItem);
    }

}
