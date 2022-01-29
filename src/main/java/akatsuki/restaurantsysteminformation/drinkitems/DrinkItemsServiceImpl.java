package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItemService;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsInvalidStateException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotContainedException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrinkItemsServiceImpl implements DrinkItemsService {
    private final DrinkItemsRepository drinkItemsRepository;
    private final UnregisteredUserService unregisteredUserService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final DrinkItemService drinkItemService;
    private final RestaurantTableService restaurantTableService;

    @Override
    public DrinkItems findOneWithItems(long id) {
        return drinkItemsRepository.findOneWithItems(id).orElseThrow(
                () -> new DrinkItemsNotFoundException("Drink items with the id " + id + " is not found in the database.")
        );
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
        Order order;
        if (drinkItemsDTO.getOrderId() == 0) {
            order = orderService.create(drinkItemsDTO.getOrderCreateDTO());
        } else {
            order = orderService.getOneWithAll((long) drinkItemsDTO.getOrderId());
        }

        List<DrinkItemUpdateDTO> drinkItemsDTOList = drinkItemsDTO.getDrinkItems();
        checkDrinks(drinkItemsDTOList);

        List<DrinkItem> drinkItemsOfList = getDrinks(drinkItemsDTOList);
        DrinkItems drinkItems = new DrinkItems(drinkItemsDTO.getNotes(), LocalDateTime.now(), false, ItemState.ON_HOLD, null, drinkItemsOfList, true);
        DrinkItems savedDrinkItems = drinkItemsRepository.save(drinkItems);

        order.getDrinks().add(savedDrinkItems);
        orderService.updateTotalPriceAndSave(order);
        return drinkItems;
    }


    @Override
    public DrinkItems update(DrinkItemsUpdateDTO drinkItemsDTO, long id) {

        Order order = orderService.getOneWithAll((long) drinkItemsDTO.getOrderId());
        List<DrinkItemUpdateDTO> drinkItemsDTOList = drinkItemsDTO.getDrinkItems();
        checkDrinksForUpdate(drinkItemsDTOList);

        DrinkItems drinkItems = findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(id);
        boolean listIsInOrder = order.getDrinks().contains(drinkItems);
        if (!listIsInOrder)
            throw new DrinkItemsNotContainedException("Drink items list with id " + id + " is not contained within order drinks.");

        ItemState itemState = drinkItems.getState();
        if (!itemState.equals(ItemState.ON_HOLD))
            throw new DrinkItemsInvalidStateException("Cannot change drink items list, because its state is " + itemState.name().toLowerCase() + ". Allowed state to change is 'on_hold'.");

        List<DrinkItem> drinkItemsNew = new ArrayList<>();

        for (DrinkItemUpdateDTO drinkItemUpdateDTO : drinkItemsDTOList) {
            if (drinkItemUpdateDTO.getStatus() == 0) {
                //create
                DrinkItem newDrinkItem = drinkItemService.create(drinkItemUpdateDTO);
                drinkItemsNew.add(newDrinkItem);
            } else if (drinkItemUpdateDTO.getStatus() == 1) {
                //update
                DrinkItem updatedDrinkItem = drinkItemService.update(drinkItemUpdateDTO, drinkItemUpdateDTO.getId());
                drinkItemsNew.add(updatedDrinkItem);
            } else if (drinkItemUpdateDTO.getStatus() == 2) {
                //delete
                drinkItemService.delete(drinkItemUpdateDTO.getId());
            } else {
                DrinkItem existingDrinkItem = drinkItemService.getOne(drinkItemUpdateDTO.getId());
                drinkItemsNew.add(existingDrinkItem);
            }
        }

        drinkItems.setDrinkItemList(drinkItemsNew);
        drinkItems.setNotes(drinkItemsDTO.getNotes());
        drinkItemsRepository.save(drinkItems);
        int index = 0;
        for (DrinkItems diInOrder : order.getDrinks()) {
            if (diInOrder.getId() == id) {
                break;
            }
            index++;
        }
        order.getDrinks().set(index, drinkItems);
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

        Order order = orderService.getOneByOrderItem(drinkItems);

        if (drinkItems.getState().equals(ItemState.ON_HOLD)) {
            drinkItems.setState(ItemState.PREPARATION);
            drinkItems.setBartender(bartender);
            restaurantTableService.changeStateOfTableWithOrder(order, TableState.CHANGED);
        } else if (drinkItems.getState().equals(ItemState.PREPARATION)) {
            drinkItems.setState(ItemState.READY);
            restaurantTableService.changeStateOfTableWithOrder(order, TableState.CHANGED);
        } else
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
        DrinkItems drinkItems = drinkItemsRepository.findById(id).orElseThrow(
                () -> new DishItemNotFoundException("Dish item with the id " + id + " is not found in the database."));
        Order order = orderService.getOneByOrderItem(drinkItems);
        order.getDrinks().remove(drinkItems);
        orderService.updateTotalPriceAndSave(order);
        drinkItemsRepository.delete(drinkItems);
    }

    @Override
    public boolean isBartenderActive(UnregisteredUser user) {
        return drinkItemsRepository.findAllByActiveIsTrueAndBartender(user).isEmpty();
    }

    private void checkDrinksForUpdate(List<DrinkItemUpdateDTO> drinkItemsDTOList) {
        drinkItemsDTOList.forEach(drinkItem -> {
            if (drinkItem.getId() != -1) {
                drinkItemService.findByIdAndFetchItem(drinkItem.getId()).getItem();
            }
        });
    }


    private void checkDrinks(List<DrinkItemUpdateDTO> drinkItems) {
        drinkItems.forEach(drinkItem -> {
//            Item item = drinkItemService.findByIdAndFetchItem(drinkItem.getId()).getItem();
            Item item = itemService.getOne(drinkItem.getItemId());
            if (item.getType() != ItemType.DRINK)
                throw new ItemNotFoundException("Not correct type of drink item!");
        });
    }

    private List<DrinkItem> getDrinks(List<DrinkItemUpdateDTO> drinkItemsDTOList) {
        List<DrinkItem> drinkItemsOfList = new ArrayList<>();

        for (DrinkItemUpdateDTO drinkItemDTO : drinkItemsDTOList) {
            itemService.getOne(drinkItemDTO.getItemId());
            DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO();
            drinkItemUpdateDTO.setAmount(drinkItemDTO.getAmount());
            drinkItemUpdateDTO.setItemId(drinkItemDTO.getItemId());
            DrinkItem savedDrinkItem = drinkItemService.create(drinkItemUpdateDTO);
            drinkItemsOfList.add(savedDrinkItem);
        }
        return drinkItemsOfList;
    }

    @Override
    public void save(DrinkItems drinkItems) {
        drinkItemsRepository.save(drinkItems);
    }
}
