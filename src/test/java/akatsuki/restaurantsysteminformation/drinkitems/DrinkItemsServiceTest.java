package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItemService;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItemServiceImpl;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsInvalidStateException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotContainedException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.item.ItemServiceImpl;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.order.OrderServiceImpl;
import akatsuki.restaurantsysteminformation.order.exception.OrderNotFoundException;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableServiceImpl;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserServiceImpl;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DrinkItemsServiceTest {

    @InjectMocks
    DrinkItemsServiceImpl drinkItemsService;
    @Mock
    DrinkItemsRepository drinkItemsRepositoryMock;
    @Mock
    UnregisteredUserServiceImpl unregisteredUserServiceMock;
    @Mock
    OrderServiceImpl orderServiceMock;
    @Mock
    ItemServiceImpl itemServiceMock;
    @Mock
    DrinkItemServiceImpl drinkItemServiceMock;
    @Mock
    RestaurantTableServiceImpl restaurantTableServiceMock;

    @Test
    public void findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered_NegativeId_ExceptionThrown() {
        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(8000L));
    }

    @Test
    public void findOneActiveAndFetchItemAndChef_ValidId_ReturnedObject() {
        DrinkItems drinkItems = new DrinkItems();

        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
        DrinkItems foundDrinkItems = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L);

        Assertions.assertEquals(foundDrinkItems, drinkItems);
    }

    @Test
    void findAllActiveAndFetchBartenderAndItems_DrinkItemsExist_ReturnedList() {
        List<DrinkItems> list = new ArrayList<>();
        list.add(new DrinkItems());
        List<DrinkItems> list2 = new ArrayList<>();
        list.add(new DrinkItems());
        Mockito.when(drinkItemsRepositoryMock.findAllActiveAndFetchBartenderAndItemsAndStateIsPreparationOrReady()).thenReturn(list);
        Mockito.when(drinkItemsRepositoryMock.findAllActiveAndFetchItemsAndStateIsOnHold()).thenReturn(list2);

        List<DrinkItems> foundList = drinkItemsService.findAllActiveAndFetchBartenderAndItems();
        Assertions.assertEquals(foundList.size(), 2);
    }
    @Test
    public void create_ValidDto_SavedObject() {
        List<DrinkItemUpdateDTO> drinkItemList = new ArrayList<>();
        drinkItemList.add(new DrinkItemUpdateDTO(2, 1L, 1L, 3));
        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, drinkItemList, "Notes", null);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item("Coca Cola", "Nice",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("soda", ItemType.DRINK), new ArrayList<>());
        DrinkItems drinkItems = new DrinkItems();
        order.getDrinks().add(drinkItems);

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        drinkItemsService.create(drinkItemsCreateDTO);
        Mockito.verify(drinkItemsRepositoryMock, Mockito.times(1)).save(Mockito.any(DrinkItems.class));
    }

    @Test
    public void create_InvalidItemTypeDto_ExceptionThrown() {
        List<DrinkItemUpdateDTO> drinkItemList = new ArrayList<>();
        drinkItemList.add(new DrinkItemUpdateDTO(2, 1L, 1L, 3));
        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, drinkItemList, "Notes", null);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item();
        item.setType(ItemType.DISH);

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(ItemNotFoundException.class, () -> drinkItemsService.create(drinkItemsCreateDTO));
    }

    @Test
    public void update_ValidDtoAndId_ChangedObject() {
        List<DrinkItemUpdateDTO> drinkItemsUpdate = new ArrayList<>();
        DrinkItemUpdateDTO diuDTO = new DrinkItemUpdateDTO(14, 1L, 3L, 1);
        drinkItemsUpdate.add(diuDTO);
        DrinkItemsUpdateDTO dto = new DrinkItemsUpdateDTO(1, drinkItemsUpdate, "Notes");
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.ON_HOLD, null, new ArrayList<>(), true);
        drinkItems.setId(6L);
        List<DrinkItems> di = new ArrayList<>();
        di.add(drinkItems);
        drinkItems.getDrinkItemList().add(new DrinkItem());
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), di);
        Item item = new Item("Coca Cola", "Nice",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("soda", ItemType.DRINK), new ArrayList<>());
        DrinkItem drinkItem = new DrinkItem();
        drinkItem.setItem(item);

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(drinkItemServiceMock.findByIdAndFetchItem(3L)).thenReturn(drinkItem);
        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(6L)).thenReturn(Optional.of(drinkItems));
        Mockito.when(drinkItemServiceMock.update(diuDTO, 3L)).thenReturn(new DrinkItem());

        drinkItemsService.update(dto, 6L);
        Mockito.verify(drinkItemsRepositoryMock, Mockito.times(1)).save(Mockito.any(DrinkItems.class));
    }

    @Test
    public void update_InvalidItemType_ExceptionThrown() {
        List<DrinkItemUpdateDTO> drinkItemsUpdate = new ArrayList<>();
        DrinkItemUpdateDTO diuDTO = new DrinkItemUpdateDTO(14, 1L, 3L, 1);
        drinkItemsUpdate.add(diuDTO);
        DrinkItemsUpdateDTO dto = new DrinkItemsUpdateDTO(1, drinkItemsUpdate, "Notes");
        Item item = new Item("Coca Cola", "Nice",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("soda", ItemType.DRINK), new ArrayList<>());
        DrinkItem drinkItem = new DrinkItem();
        drinkItem.setItem(item);
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.READY, null, new ArrayList<>(), true);
        drinkItems.setId(1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), Collections.singletonList(drinkItems));

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(drinkItemServiceMock.findByIdAndFetchItem(3L)).thenReturn(drinkItem);
        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));

        Assertions.assertThrows(DrinkItemsInvalidStateException.class, () -> drinkItemsService.update(dto, 1L));
    }

    @Test
    void changeStateOfDrinkItems_ValidIds_FromOnHoldToPreparation() {
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.ON_HOLD, null, new ArrayList<>(), true);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.BARTENDER, false, "1111");

        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        DrinkItems changedDrinkItems = drinkItemsService.changeStateOfDrinkItems(1L, 1L);

        Assertions.assertEquals(changedDrinkItems.getState(), ItemState.PREPARATION);
        Mockito.verify(drinkItemsRepositoryMock, Mockito.times(1)).save(drinkItems);
    }

    @Test
    void changeStateOfDrinkItems_ValidIds_FromPreparationToReady() {
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.PREPARATION, null, new ArrayList<>(), true);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.BARTENDER, false, "1111");

        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        DrinkItems changedDrinkItems = drinkItemsService.changeStateOfDrinkItems(1L, 1L);

        Assertions.assertEquals(changedDrinkItems.getState(), ItemState.READY);
        Mockito.verify(drinkItemsRepositoryMock, Mockito.times(1)).save(drinkItems);
    }

    @Test
    void changeStateOfDrinkItems_ValidIds_FromReadyToDelivered() {
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.READY, null, new ArrayList<>(), true);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.WAITER, false, "1111");

        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        DrinkItems changedDrinkItems = drinkItemsService.changeStateOfDrinkItems(1L, 1L);

        Assertions.assertEquals(changedDrinkItems.getState(), ItemState.DELIVERED);
        Mockito.verify(drinkItemsRepositoryMock, Mockito.times(1)).save(drinkItems);
    }

    @Test
    void changeStateOfDrinkItems_InvalidItemId_ExceptionThrown() {
        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.changeStateOfDrinkItems(1000L, 1L));
    }

    @Test
    void changeStateOfDrinkItems_NotAllowedUserType_ExceptionThrown() {
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.READY, null, new ArrayList<>(), true);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.BARTENDER, false, "1111");

        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> drinkItemsService.changeStateOfDrinkItems(1L, 1L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.ON_HOLD, null, new ArrayList<>(), true);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        order.getDrinks().add(drinkItems);

        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
        Mockito.when(orderServiceMock.getOneByOrderItem(drinkItems)).thenReturn(order);

        drinkItemsService.delete(1L);

        Assertions.assertTrue(drinkItems.isDeleted());
        Assertions.assertFalse(drinkItems.isActive());
        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
        Mockito.verify(drinkItemsRepositoryMock, Mockito.times(1)).save(drinkItems);
    }

    @Test
    public void delete_InvalidId_ExceptionThrown() {
        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.READY, null, new ArrayList<>(), true);
        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
        Assertions.assertThrows(DrinkItemsInvalidStateException.class, () -> drinkItemsService.delete(1L));
    }

    @Test
    public void delete_InvalidState_ExceptionThrown() {
        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.delete(1000L));
    }

    @Test
    void isBartenderActive_ContainsUser_False() {
        UnregisteredUser user = new UnregisteredUser();
        List<DrinkItems> drinkItemsList = Collections.singletonList(new DrinkItems());

        Mockito.when(drinkItemsRepositoryMock.findAllByActiveIsTrueAndBartender(user)).thenReturn(drinkItemsList);

        boolean isActive = drinkItemsService.isBartenderActive(user);
        Assertions.assertFalse(isActive);
    }

    @Test
    void isBartenderActive_ContainsUser_True() {
        UnregisteredUser user = new UnregisteredUser();
        Mockito.when(drinkItemsRepositoryMock.findAllByActiveIsTrueAndBartender(user)).thenReturn(new ArrayList<>());

        boolean isActive = drinkItemsService.isBartenderActive(user);
        Assertions.assertTrue(isActive);
    }

}