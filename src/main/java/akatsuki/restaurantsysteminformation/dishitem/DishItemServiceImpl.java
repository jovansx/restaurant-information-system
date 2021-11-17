package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidStateException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidTypeException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemOrderException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishItemServiceImpl implements DishItemService {
    private final DishItemRepository dishItemRepository;
    private final UnregisteredUserService unregisteredUserService;
    private final OrderService orderService;
    private final ItemService itemService;

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
    public void create(DishItemCreateDTO itemCreateDTO) {
        Order order = orderService.getOneWithAll(itemCreateDTO.getOrderId());
        Item item = itemService.getOne(itemCreateDTO.getItemId());
        if (!item.getType().equals(ItemType.DISH))
            throw new DishItemInvalidTypeException("Item type is not DISH.");
        DishItem dishItem = new DishItem(itemCreateDTO.getNotes(), LocalDateTime.now(), false, ItemState.NEW, itemCreateDTO.getAmount(), null, item, true);
        dishItem = dishItemRepository.save(dishItem);
        order.getDishes().add(dishItem);
        orderService.updateTotalPriceAndSave(order);
    }

    @Override
    public void update(DishItemCreateDTO itemCreateDTO, long id) {
        Order order = orderService.getOneWithAll(itemCreateDTO.getOrderId());
        Item item = itemService.getOne(itemCreateDTO.getItemId());
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
    }

    @Override
    public void prepare(long id, long waiterId) {
        DishItem dishItem = getOneActive(id);
        if (!dishItem.getState().equals(ItemState.NEW))
            throw new DishItemInvalidStateException("Cannot preapre dish item, because its state is " + dishItem.getState().name().toLowerCase() + ".");
        Order order = orderService.getOneByOrderItem(dishItem);
        if (!order.getWaiter().getId().equals(waiterId))
            throw new DishItemOrderException("Dish item does not belong to waiter with the id of  " + waiterId + ".");
        dishItem.setState(ItemState.ON_HOLD);
        dishItemRepository.save(dishItem);
    }

    @Override
    public DishItem changeStateOfDishItems(long itemId, long userId) {
        DishItem dishItem = dishItemRepository.findOneActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered(itemId).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + itemId + " is not found in the database.")
        );
        UserType typeOfAllowedUser;
        if (dishItem.getState().equals(ItemState.READY))
            typeOfAllowedUser = UserType.WAITER;
        else if (dishItem.getState().equals(ItemState.ON_HOLD) || dishItem.getState().equals(ItemState.PREPARATION))
            typeOfAllowedUser = UserType.CHEF;
        else
            throw new DishItemNotFoundException("Dish item with state of  " + dishItem.getState().name() + " is not valid for changing states.");

        UnregisteredUser chef = this.unregisteredUserService.getOne(userId);
        if (!chef.getType().equals(typeOfAllowedUser))
            throw new UserNotFoundException("User with the id " + userId + " is not a " + typeOfAllowedUser.name().toLowerCase() + ".");

        if (dishItem.getState().equals(ItemState.ON_HOLD)) {
            dishItem.setState(ItemState.PREPARATION);
            dishItem.setChef(chef);
        } else if (dishItem.getState().equals(ItemState.PREPARATION))
            dishItem.setState(ItemState.READY);
        else
            dishItem.setState(ItemState.DELIVERED);
        dishItemRepository.save(dishItem);
        return dishItem;
    }

    @Override
    public void delete(long id) {
        DishItem dishItem = getOneActive(id);

        Order order = orderService.getOneByOrderItem(dishItem);
        order.getDishes().remove(dishItem);
        orderService.updateTotalPriceAndSave(order);

        dishItem.setDeleted(true);
        dishItem.setActive(false);
        dishItemRepository.save(dishItem);
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

}
