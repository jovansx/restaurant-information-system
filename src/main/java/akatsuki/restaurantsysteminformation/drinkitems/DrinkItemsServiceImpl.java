package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItemService;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsInvalidStateException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotContainedException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrinkItemsServiceImpl implements DrinkItemsService {
    private final DrinkItemsRepository drinkItemsRepository;
    private final UnregisteredUserService unregisteredUserService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final DrinkItemService drinkItemService;

    @Override
    public DrinkItems getOne(long id) {
        return drinkItemsRepository.findById(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public DrinkItems findOneWithItems(long id) {
        return drinkItemsRepository.findOneWithItems(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<DrinkItems> getAll() {
        return drinkItemsRepository.findAll();
    }

    @Override
    public DrinkItems findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(long id) {
        return drinkItemsRepository.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + id + " are not found in the database.")
        );
    }

    @Override
    public DrinkItems findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(long id) {
        return drinkItemsRepository.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + id + " are not found in the database.")
        );
    }

    @Override
    public List<DrinkItems> findAllActiveAndFetchBartenderAndItems() {
        List<DrinkItems> drinkItemsList = drinkItemsRepository.findAllActiveAndFetchBartenderAndItemsAndStateIsPreparationOrReady();
        drinkItemsList.addAll(drinkItemsRepository.findAllActiveAndFetchItemsAndStateIsOnHold());
        return drinkItemsList;
    }

    @Override
    public DrinkItems create(DrinkItemsCreateDTO drinkItemsDTO) {
        Order order = orderService.getOneWithAll((long) drinkItemsDTO.getOrderId());
        List<DrinkItemCreateDTO> drinkItemsDTOList = drinkItemsDTO.getDrinkItemList();
        checkDrinks(drinkItemsDTOList);

        List<DrinkItem> drinkItemsOfList = getDrinks(drinkItemsDTOList);
        DrinkItems drinkItems = new DrinkItems(drinkItemsDTO.getNotes(), LocalDateTime.now(), false, ItemState.ON_HOLD, null, drinkItemsOfList, true);
        DrinkItems savedDrinkItems = drinkItemsRepository.save(drinkItems);

        order.getDrinks().add(savedDrinkItems);
        orderService.updateTotalPriceAndSave(order);
        return drinkItems;
    }


    @Override
    public DrinkItems update(DrinkItemsCreateDTO drinkItemsDTO, long id) {
        Order order = orderService.getOneWithAll((long) drinkItemsDTO.getOrderId());
        List<DrinkItemCreateDTO> drinkItemsDTOList = drinkItemsDTO.getDrinkItemList();
        checkDrinks(drinkItemsDTOList);

        DrinkItems drinkItems = findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(id);
        boolean listIsInOrder = order.getDrinks().contains(drinkItems);
        if (!listIsInOrder)
            throw new DrinkItemsNotContainedException("Drink items list with id " + id + " is not contained within order drinks.");

        ItemState itemState = drinkItems.getState();
        if (!itemState.equals(ItemState.ON_HOLD))
            throw new DrinkItemsInvalidStateException("Cannot change drink items list, because its state is " + itemState.name().toLowerCase() + ". Allowed state to change is 'on_hold'.");

        List<DrinkItem> ref = new ArrayList<>(drinkItems.getDrinkItemList());
        drinkItems.getDrinkItemList().clear();
        drinkItemsRepository.save(drinkItems);
        for (DrinkItem drinkItem : ref)
            drinkItemService.delete(drinkItem);
        List<DrinkItem> drinkItemsOfList = getDrinks(drinkItemsDTOList);
        drinkItems.setDrinkItemList(drinkItemsOfList);
        drinkItems.setNotes(drinkItemsDTO.getNotes());
        drinkItemsRepository.save(drinkItems);

        orderService.updateTotalPriceAndSave(order);
        return drinkItems;
    }

    @Override
    public DrinkItems changeStateOfDrinkItems(long itemId, long userId) {
        DrinkItems drinkItems = findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(itemId);
        UserType typeOfAllowedUser;
        if (drinkItems.getState().equals(ItemState.READY))
            typeOfAllowedUser = UserType.WAITER;
        else
            typeOfAllowedUser = UserType.BARTENDER;

        UnregisteredUser bartender = this.unregisteredUserService.getOne(userId);
        if (!bartender.getType().equals(typeOfAllowedUser))
            throw new UserNotFoundException("User with the id " + userId + " is not a " + typeOfAllowedUser.name().toLowerCase() + ".");

        if (drinkItems.getState().equals(ItemState.ON_HOLD)) {
            drinkItems.setState(ItemState.PREPARATION);
            drinkItems.setBartender(bartender);
        } else if (drinkItems.getState().equals(ItemState.PREPARATION))
            drinkItems.setState(ItemState.READY);
        else
            drinkItems.setState(ItemState.DELIVERED);
        drinkItemsRepository.save(drinkItems);
        return drinkItems;
    }

    @Override
    public DrinkItems delete(long id) {
        DrinkItems drinkItems = findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(id);
        if (!drinkItems.getState().equals(ItemState.ON_HOLD))
            throw new DrinkItemsInvalidStateException("Drink items with state of " + drinkItems.getState().name().toLowerCase() + " cannot be deleted.");
        Order order = orderService.getOneByOrderItem(drinkItems);
        order.getDrinks().remove(drinkItems);
        orderService.updateTotalPriceAndSave(order);

        drinkItems.setDeleted(true);
        drinkItems.setActive(false);
        drinkItemsRepository.save(drinkItems);
        return drinkItems;
    }

    @Override
    public void deleteById(long id) {
        DrinkItems drinkItems = getOne(id);
        Order order = orderService.getOneByOrderItem(drinkItems);
        order.getDrinks().remove(drinkItems);
        orderService.updateTotalPriceAndSave(order);
        drinkItemsRepository.delete(drinkItems);
    }

    @Override
    public boolean isBartenderActive(UnregisteredUser user) {
        return drinkItemsRepository.findAllByActiveIsTrueAndBartender(user).isEmpty();
    }


    private void checkDrinks(List<DrinkItemCreateDTO> drinkItems) {
        drinkItems.forEach(drinkItem -> {
            Item item = itemService.getOne(drinkItem.getItemId());
            if (item.getType() != ItemType.DRINK)
                throw new ItemNotFoundException("Not correct type of drink item!");
        });
    }

    private List<DrinkItem> getDrinks(List<DrinkItemCreateDTO> drinkItemsDTOList) {
        List<DrinkItem> drinkItemsOfList = new ArrayList<>();

        for (DrinkItemCreateDTO drinkItemDTO : drinkItemsDTOList) {
            Item item = itemService.getOne(drinkItemDTO.getItemId());
            DrinkItem drinkItem = new DrinkItem(drinkItemDTO.getAmount(), item);
            DrinkItem savedDrinkItem = drinkItemService.create(drinkItem);
            drinkItemsOfList.add(savedDrinkItem);
        }
        return drinkItemsOfList;
    }

    @Override
    public void save(DrinkItems drinkItems) {
        drinkItemsRepository.save(drinkItems);
    }
}
