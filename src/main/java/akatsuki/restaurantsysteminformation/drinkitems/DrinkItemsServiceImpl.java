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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    public void delete(long id) {
        DrinkItems drinkItems = getOne(id);
        drinkItems.setDeleted(true);
        drinkItemsRepository.save(drinkItems); //TODO vidi sta raditi sa povezanim entitetima DrinkItem
    }

    @Override
    public void create(DrinkItemsCreateDTO drinkItemsDTO) {
        Order order = orderService.getOne(drinkItemsDTO.getOrderId());
        List<DrinkItemCreateDTO> drinkItemsDTOList = drinkItemsDTO.getDrinkItemList();
        checkDrinks(drinkItemsDTOList);

        List<DrinkItem> drinkItemsOfList = getDrinks(order, drinkItemsDTOList);
        DrinkItems drinkItems = new DrinkItems(drinkItemsDTO.getNotes(), LocalDateTime.now(), false, ItemState.ON_HOLD, null, drinkItemsOfList, true);
        DrinkItems savedDrinkItems = drinkItemsRepository.save(drinkItems);

        orderService.addDrinkItemsToCollection(savedDrinkItems, order);
    }

    @Override
    public void update(DrinkItemsCreateDTO drinkItemsDTO, long id) {
        Order order = orderService.getOne(drinkItemsDTO.getOrderId()); //TODO Obrati paznju, kad budemo promenili in-view na false, moraces fetchovati(jos na nekim mestima)
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

        List<DrinkItem> ref = new ArrayList<>(drinkItems.getDrinkItems());
        drinkItems.getDrinkItems().clear();
        ref.forEach(drinkItem -> drinkItemService.delete(drinkItem));

        List<DrinkItem> drinkItemsOfList = getDrinks(order, drinkItemsDTOList);
        drinkItems.setDrinkItems(drinkItemsOfList);
        drinkItemsRepository.save(drinkItems);
        
        orderService.updateTotalPrice(order);
    }

    private void checkDrinks(List<DrinkItemCreateDTO> drinkItems) {
        drinkItems.forEach(drinkItem -> {
            Item item = itemService.getOne(drinkItem.getItemId());
            if (item.getType() != ItemType.DRINK) {
                throw new ItemNotFoundException("Not correct type of drink item!");
            }
        });
    }

    private List<DrinkItem> getDrinks(Order order, List<DrinkItemCreateDTO> drinkItemsDTOList) {
        double totalPrice = 0;
        List<DrinkItem> drinkItemsOfList = new ArrayList<>();

        for (DrinkItemCreateDTO drinkItemDTO: drinkItemsDTOList) {
            Item item = itemService.getOne(drinkItemDTO.getItemId());
            DrinkItem drinkItem = new DrinkItem(drinkItemDTO.getAmount(), item);
            DrinkItem savedDrinkItem = drinkItemService.create(drinkItem);
            drinkItemsOfList.add(savedDrinkItem);
            totalPrice += savedDrinkItem.getAmount() * item.getLastDefinedPrice();
        }
        order.setTotalPrice(totalPrice);
        return drinkItemsOfList;
    }
}
