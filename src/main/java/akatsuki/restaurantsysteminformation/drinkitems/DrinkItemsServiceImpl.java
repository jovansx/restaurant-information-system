package akatsuki.restaurantsysteminformation.drinkitems;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DrinkItemsServiceImpl implements DrinkItemsService {
    private DrinkItemsRepository drinkItemsRepository;
    private UnregisteredUserService unregisteredUserService;
    private OrderService orderService;
    private ItemService itemService;
    private DrinkItemService drinkItemService;

    @Autowired
    public void setDrinkItemsRepository(DrinkItemsRepository drinkItemsRepository, UnregisteredUserService unregisteredUserService,
                                        OrderService orderService, ItemService itemService, DrinkItemService drinkItemService) {
        this.drinkItemsRepository = drinkItemsRepository;
        this.unregisteredUserService = unregisteredUserService;
        this.orderService = orderService;
        this.itemService = itemService;
        this.drinkItemService = drinkItemService;
    }

    @Override
    public DrinkItems getOne(long id) {
        return drinkItemsRepository.findById(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + id + " are not found in the database.")
        );
    }

    @Override
    public boolean isBartenderActive(UnregisteredUser user) {
        return drinkItemsRepository.findAllByActiveIsTrueAndBartender(user).isEmpty();
    }

    @Override
    public List<DrinkItems> getAll() {
        return drinkItemsRepository.findAllFetchBartender().orElseThrow(
                () -> new DrinkItemsNotFoundException("There's no drink items list created."));
    }

    @Override
    public DrinkItems getOneActive(long id) {
        return drinkItemsRepository.findOneActiveWithBartenderAndWithItems(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + id + " are not found in the database.")
        );
    }

    @Override
    public List<DrinkItems> getAllActive() {
        List<DrinkItems> drinkItemsList = drinkItemsRepository.findAllNotOnHoldActive();
        drinkItemsList.addAll(drinkItemsRepository.findAllOnHoldActive());
        return drinkItemsList;
    }

    @Override
    public DrinkItems changeStateOfDrinkItems(long itemId, long userId) {
        DrinkItems drinkItems = getOne(itemId);
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
    public void delete(long id) {
        DrinkItems drinkItems = getOneActive(id);

        Order order = orderService.getOrderByOrderItem(drinkItems);
        order.getDrinks().remove(drinkItems);
        orderService.updateTotalPriceAndSave(order);

        drinkItems.setDeleted(true);
        drinkItems.setActive(false);
        drinkItemsRepository.save(drinkItems);
    }

    @Override
    public void create(DrinkItemsCreateDTO drinkItemsDTO) {
        Order order = orderService.getOneWithDrinks(drinkItemsDTO.getOrderId());
        List<DrinkItemCreateDTO> drinkItemsDTOList = drinkItemsDTO.getDrinkItemList();
        checkDrinks(drinkItemsDTOList);

        List<DrinkItem> drinkItemsOfList = getDrinks(drinkItemsDTOList);
        DrinkItems drinkItems = new DrinkItems(drinkItemsDTO.getNotes(), LocalDateTime.now(), false, ItemState.ON_HOLD, null, drinkItemsOfList, true);
        DrinkItems savedDrinkItems = drinkItemsRepository.save(drinkItems);

        order.getDrinks().add(savedDrinkItems);
        orderService.updateTotalPriceAndSave(order);
    }

    @Override
    public void update(DrinkItemsCreateDTO drinkItemsDTO, long id) {
        Order order = orderService.getOneWithDrinks(drinkItemsDTO.getOrderId());
        List<DrinkItemCreateDTO> drinkItemsDTOList = drinkItemsDTO.getDrinkItemList();
        checkDrinks(drinkItemsDTOList);

        DrinkItems drinkItems = getOne(id);
        boolean listIsInOrder = order.getDrinks().contains(drinkItems);
        if (!listIsInOrder) {
            throw new DrinkItemsNotContainedException("Drink items list with id " + id + " is not contained within order drinks.");
        }

        ItemState itemState = drinkItems.getState();
        if (!itemState.equals(ItemState.ON_HOLD)) {
            throw new DrinkItemsInvalidStateException("Cannot change drink items list, because its state is " + itemState.name().toLowerCase() + ". Allowed state to change is 'on_hold'.");
        }

        List<DrinkItem> ref = new ArrayList<>(drinkItems.getDrinkItemList());
        drinkItems.getDrinkItemList().clear();
        ref.forEach(drinkItem -> drinkItemService.delete(drinkItem));

        List<DrinkItem> drinkItemsOfList = getDrinks(drinkItemsDTOList);
        drinkItems.setDrinkItemList(drinkItemsOfList);
        drinkItemsRepository.save(drinkItems);

        orderService.updateTotalPriceAndSave(order);
    }

    private void checkDrinks(List<DrinkItemCreateDTO> drinkItems) {
        drinkItems.forEach(drinkItem -> {
            Item item = itemService.getOne(drinkItem.getItemId());
            if (item.getType() != ItemType.DRINK) {
                throw new ItemNotFoundException("Not correct type of drink item!");
            }
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
}
