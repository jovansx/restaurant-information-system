package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItemService;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsInvalidStateException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
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
    UnregisteredUserService unregisteredUserServiceMock;
    @Mock
    OrderService orderServiceMock;
    @Mock
    ItemService itemServiceMock;
    @Mock
    DrinkItemService drinkItemServiceMock;

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


//    @Test
//    public void create_ValidDto_SavedObject() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, list, "Notes");
//        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
//        Item item = new Item("Coca Cola", "Nice",
//                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("soda", CategoryType.DRINK), new ArrayList<>());
//
//        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
//        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);
//        Mockito.when(drinkItemServiceMock.create(Mockito.any(DrinkItem.class))).thenReturn(new DrinkItem());
//        Mockito.when(drinkItemsRepositoryMock.save(Mockito.any(DrinkItems.class))).thenReturn(new DrinkItems());
//
//        drinkItemsService.create(drinkItemsCreateDTO);
//
//        Assertions.assertEquals(order.getDrinks().size(), 1);
//        Mockito.verify(drinkItemsRepositoryMock, Mockito.times(1)).save(Mockito.any(DrinkItems.class));
//        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
//    }

//    @Test
//    public void create_InvalidItemTypeDto_ExceptionThrown() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, list, "Notes");
//        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
//        Item item = new Item("Coca Cola", "Nice",
//                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("soda", CategoryType.DRINK), new ArrayList<>());
//
//        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
//        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);
//
//        Assertions.assertThrows(ItemNotFoundException.class, () -> drinkItemsService.create(drinkItemsCreateDTO));
//    }

//    @Test
//    public void update_ValidDtoAndId_ChangedObject() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, list, "Notes");
//        Item item = new Item("Coca Cola", "Nice",
//                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("soda", CategoryType.DRINK), new ArrayList<>());
//        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.ON_HOLD, null, new ArrayList<>(), true);
//        drinkItems.setId(1L);
//        drinkItems.getDrinkItemList().add(new DrinkItem());
//        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), Collections.singletonList(drinkItems));
//
//        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
//        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);
//        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
//        Mockito.when(drinkItemServiceMock.create(Mockito.any(DrinkItem.class))).thenReturn(new DrinkItem());
//        Mockito.when(drinkItemsRepositoryMock.save(Mockito.any(DrinkItems.class))).thenReturn(new DrinkItems());
//
//        drinkItemsService.update(dto, 1L);
//
//        Assertions.assertEquals(drinkItems.getDrinkItemList().size(), 1);
//        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
//    }

//    @Test
//    public void update_InvalidItemType_ExceptionThrown() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, list, "Notes");
//        Item item = new Item("Coca Cola", "Nice",
//                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("soda", CategoryType.DRINK), new ArrayList<>());
//        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.READY, null, new ArrayList<>(), true);
//        drinkItems.setId(1L);
//        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), Collections.singletonList(drinkItems));
//
//        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
//        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);
//        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
//
//        Assertions.assertThrows(DrinkItemsInvalidStateException.class, () -> drinkItemsService.update(dto, 1L));
//    }

//    @Test
//    public void update_InvalidOrderId_ExceptionThrown() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, list, "Notes");
//        Item item = new Item("Coca Cola", "Nice",
//                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("soda", CategoryType.DRINK), new ArrayList<>());
//        DrinkItems drinkItems = new DrinkItems("Old note.", LocalDateTime.now(), false, ItemState.ON_HOLD, null, new ArrayList<>(), true);
//        drinkItems.setId(1L);
//        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
//
//        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
//        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);
//        Mockito.when(drinkItemsRepositoryMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(1L)).thenReturn(Optional.of(drinkItems));
//
//        Assertions.assertThrows(DrinkItemsNotContainedException.class, () -> drinkItemsService.update(dto, 1L));
//    }

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