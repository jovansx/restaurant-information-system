package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidStateException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidTypeException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemOrderException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishItemServiceImpl implements DishItemService {
    private final DishItemRepository dishItemRepository;
    private final UnregisteredUserService unregisteredUserService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final RestaurantTableService restaurantTableService;

    @Override
    public DishItem getOne(long id) {
        return dishItemRepository.findById(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<DishItem> getAll() {
        return dishItemRepository.findAll();
    }

    @Override
    public DishItem findOneActiveAndFetchItemAndChef(long id) {
        return dishItemRepository.findOneActiveAndFetchItemAndChef(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<DishItem> findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered() {
        return dishItemRepository.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();
    }

    @Override
    public DishItem create(DishItemCreateDTO itemCreateDTO) {
        Order order = orderService.getOneWithAll(itemCreateDTO.getOrderId());
        Item item = itemService.getOne(itemCreateDTO.getItemId());
        if (!item.getType().equals(ItemType.DISH))
            throw new DishItemInvalidTypeException("Item type is not DISH.");
        DishItem dishItem = new DishItem(itemCreateDTO.getNotes(), LocalDateTime.now(), false, ItemState.NEW, itemCreateDTO.getAmount(), null, item, true);
        dishItem = dishItemRepository.save(dishItem);
        order.getDishes().add(dishItem);
        orderService.updateTotalPriceAndSave(order);
        return dishItem;
    }

    @Override
    public DishItem update(DishItemCreateDTO itemCreateDTO, long id) {
        Order order = orderService.getOneWithAll(itemCreateDTO.getOrderId());
//        Item item = itemService.getOne(itemCreateDTO.getItemId());
        Optional<DishItem> dishItemMaybe = dishItemRepository.findByIdAndFetchItem(id);
        if(dishItemMaybe.isEmpty()) {
            throw new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.");
        }
        Item item = dishItemMaybe.get().getItem();
        if (!item.getType().equals(ItemType.DISH))
            throw new DishItemInvalidTypeException("Item type is not DISH.");
        DishItem dishItem = null;
        for (DishItem di : order.getDishes()) {
            if (di.getId().equals(id)) {
                dishItem = di;
                break;
            }
        }

        if (dishItem == null)
            throw new DishItemOrderException("Dish item order id is not equal to " + itemCreateDTO.getOrderId() + ". Order cannot be changed.");

        ItemState itemState = dishItem.getState();
        if (!itemState.equals(ItemState.NEW) && !itemState.equals(ItemState.ON_HOLD))
            throw new DishItemInvalidStateException("Cannot change dish item, because its state is " + itemState.name().toLowerCase() + ".");

        dishItem.setItem(item);
        dishItem.setAmount(itemCreateDTO.getAmount());
        dishItem.setNotes(itemCreateDTO.getNotes());
        orderService.updateTotalPriceAndSave(order);
        return dishItem;
    }

    @Override
    public DishItem changeStateOfDishItems(long itemId, long userId) {
        DishItem dishItem = dishItemRepository.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(itemId).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + itemId + " is not found in the database.")
        );
        UserType typeOfAllowedUser;
        if (dishItem.getState().equals(ItemState.NEW) || dishItem.getState().equals(ItemState.READY))
            typeOfAllowedUser = UserType.WAITER;
        else
            typeOfAllowedUser = UserType.CHEF;

        UnregisteredUser user = this.unregisteredUserService.getOne(userId);
        if (!user.getType().equals(typeOfAllowedUser))
            throw new UserTypeNotValidException("User with the id " + userId + " is not a " + typeOfAllowedUser.name().toLowerCase() + ".");

        Order order = orderService.getOneByOrderItem(dishItem);

        if (dishItem.getState().equals(ItemState.NEW)) {
            dishItem.setState(ItemState.ON_HOLD);
        } else if (dishItem.getState().equals(ItemState.ON_HOLD)) {
            dishItem.setState(ItemState.PREPARATION);
            dishItem.setChef(user);
            restaurantTableService.changeStateOfTableWithOrder(order, TableState.CHANGED);
        } else if (dishItem.getState().equals(ItemState.PREPARATION)) {
            dishItem.setState(ItemState.READY);
            restaurantTableService.changeStateOfTableWithOrder(order, TableState.CHANGED);
        } else
            dishItem.setState(ItemState.DELIVERED);

        dishItemRepository.save(dishItem);
        return dishItem;
    }

    @Override
    public DishItem delete(long id) {
        DishItem dishItem = getOneActive(id);

        Order order = orderService.getOneByOrderItem(dishItem);
        order.getDishes().remove(dishItem);
        orderService.updateTotalPriceAndSave(order);

        dishItem.setDeleted(true);
        dishItem.setActive(false);
        dishItemRepository.save(dishItem);
        return dishItem;
    }

    @Override
    public void deleteById(long id) {
        DishItem dishItem = getOneActive(id);
        Order order = orderService.getOneByOrderItem(dishItem);
        order.getDishes().remove(dishItem);
        orderService.updateTotalPriceAndSave(order);
        dishItemRepository.delete(dishItem);
    }

    @Override
    public boolean isChefActive(UnregisteredUser user) {
        return dishItemRepository.findAllByActiveIsTrueAndChef(user).isEmpty();
    }

    private DishItem getOneActive(long id) {
        return dishItemRepository.findByIdAndActiveIsTrue(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public void save(DishItem dishItem) {
        dishItemRepository.save(dishItem);
    }
}
