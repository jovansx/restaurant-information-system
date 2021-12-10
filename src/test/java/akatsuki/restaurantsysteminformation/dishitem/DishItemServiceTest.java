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
import akatsuki.restaurantsysteminformation.item.ItemServiceImpl;
import akatsuki.restaurantsysteminformation.enums.CategoryType;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderServiceImpl;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserServiceImpl;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
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
class DishItemServiceTest {

    @InjectMocks
    DishItemServiceImpl dishItemService;

    @Mock
    DishItemRepository dishItemRepositoryMock;

    @Mock
    OrderServiceImpl orderServiceMock;

    @Mock
    ItemServiceImpl itemServiceMock;

    @Mock
    UnregisteredUserServiceImpl unregisteredUserServiceMock;

    @Test
    public void findOneActiveAndFetchItemAndChef_NegativeId_ExceptionThrown() {
        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChef(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DishItemNotFoundException.class, () -> {
            dishItemService.findOneActiveAndFetchItemAndChef(8000L);
        });
    }

    @Test
    public void findOneActiveAndFetchItemAndChef_ValidId_ReturnedObject() {
        DishItem dishItem = new DishItem();

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChef(1L)).thenReturn(Optional.of(dishItem));
        DishItem foundDishItem = dishItemService.findOneActiveAndFetchItemAndChef(1L);

        Assertions.assertEquals(foundDishItem, dishItem);
    }

    @Test
    void findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered_DishItemsExist_ReturnedList() {
        List<DishItem> list = Collections.singletonList(new DishItem());
        Mockito.when(dishItemRepositoryMock.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered()).thenReturn(list);

        List<DishItem> foundList = dishItemService.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();
        Assertions.assertEquals(foundList, list);
    }

    @Test
    public void create_ValidDto_SavedObject() {
        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(1L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert", CategoryType.DISH), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        dishItemService.create(dishItemCreateDTO);

        Assertions.assertEquals(order.getDishes().size(), 1);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(Mockito.any(DishItem.class));
        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
    }

    @Test
    public void create_InvalidItemType_ExceptionThrown() {
        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(1L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("dessert", CategoryType.DISH), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemInvalidTypeException.class, () -> dishItemService.create(dishItemCreateDTO));
    }

    @Test
    public void update_ValidDtoAndId_ChangedObject() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert", CategoryType.DISH), new ArrayList<>());
        DishItem dishItem = new DishItem("Old note.", LocalDateTime.now(), false, ItemState.NEW, 5, null, null, true);
        dishItem.setId(1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, Collections.singletonList(dishItem), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        dishItemService.update(dto, 1L);

        Assertions.assertEquals(dishItem.getItem(), item);
        Assertions.assertEquals(dishItem.getAmount(), 10);
        Assertions.assertEquals(dishItem.getNotes(), "New note.");
        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
    }

    @Test
    public void update_InvalidItemType_ExceptionThrown() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("dessert", CategoryType.DISH), new ArrayList<>());
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemInvalidTypeException.class, () -> dishItemService.update(dto, 1L));
    }

    @Test
    public void update_InvalidDishItem_ExceptionThrown() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert", CategoryType.DISH), new ArrayList<>());
        DishItem dishItem = new DishItem("Old note.", LocalDateTime.now(), false, ItemState.NEW, 5, null, null, true);
        dishItem.setId(1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemOrderException.class, () -> dishItemService.update(dto, 1L));
    }

    @Test
    public void update_InvalidItemState_ExceptionThrown() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert", CategoryType.DISH), new ArrayList<>());
        DishItem dishItem = new DishItem("Old note.", LocalDateTime.now(), false, ItemState.PREPARATION, 5, null, null, true);
        dishItem.setId(1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, Collections.singletonList(dishItem), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemInvalidStateException.class, () -> dishItemService.update(dto, 1L));
    }

    @Test
    void changeStateOfDishItems_ValidIds_FromNewToOnHold() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.NEW, true, 5, null, null);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.WAITER, false, "1111");

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1L)).thenReturn(Optional.of(dishItem));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        DishItem changedDishItem = dishItemService.changeStateOfDishItems(1L, 1L);

        Assertions.assertEquals(changedDishItem.getState(), ItemState.ON_HOLD);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(dishItem);
    }

    @Test
    void changeStateOfDishItems_ValidIds_FromReadyToDelivered() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.READY, true, 5, null, null);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.WAITER, false, "1111");

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1L)).thenReturn(Optional.of(dishItem));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        DishItem changedDishItem = dishItemService.changeStateOfDishItems(1L, 1L);

        Assertions.assertEquals(changedDishItem.getState(), ItemState.DELIVERED);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(dishItem);
    }

    @Test
    void changeStateOfDishItems_ValidIds_FromOnHoldToPreparation() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.ON_HOLD, true, 5, null, null);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.CHEF, false, "1111");

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1L)).thenReturn(Optional.of(dishItem));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        DishItem changedDishItem = dishItemService.changeStateOfDishItems(1L, 1L);

        Assertions.assertEquals(changedDishItem.getState(), ItemState.PREPARATION);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(dishItem);
    }

    @Test
    void changeStateOfDishItems_ValidIds_FromPreparationToReady() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.PREPARATION, true, 5, null, null);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.CHEF, false, "1111");

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1L)).thenReturn(Optional.of(dishItem));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        DishItem changedDishItem = dishItemService.changeStateOfDishItems(1L, 1L);

        Assertions.assertEquals(changedDishItem.getState(), ItemState.READY);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(dishItem);
    }

    @Test
    void changeStateOfDishItems_InvalidItemId_ExceptionThrown() {
        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DishItemNotFoundException.class, () -> dishItemService.changeStateOfDishItems(1000L, 1L));
    }

    @Test
    void changeStateOfDishItems_NotAllowedUserType_ExceptionThrown() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.NEW, true, 5, null, null);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.CHEF, false, "1111");

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1L)).thenReturn(Optional.of(dishItem));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        Assertions.assertThrows(UserTypeNotValidException.class, () -> dishItemService.changeStateOfDishItems(1L, 1L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.NEW, true, 5, null, null);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        order.getDishes().add(dishItem);

        Mockito.when(dishItemRepositoryMock.findByIdAndActiveIsTrue(1L)).thenReturn(Optional.of(dishItem));
        Mockito.when(orderServiceMock.getOneByOrderItem(dishItem)).thenReturn(order);

        dishItemService.delete(1L);

        Assertions.assertTrue(dishItem.isDeleted());
        Assertions.assertFalse(dishItem.isActive());
        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(dishItem);
    }

    @Test
    public void delete_InvalidId_ExceptionThrown() {
        Mockito.when(dishItemRepositoryMock.findByIdAndActiveIsTrue(1000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DishItemNotFoundException.class, () -> dishItemService.delete(1000L));
    }

    @Test
    void isChefActive_ContainsUser_False() {
        UnregisteredUser user = new UnregisteredUser();
        List<DishItem> dishItemList = Collections.singletonList(new DishItem());

        Mockito.when(dishItemRepositoryMock.findAllByActiveIsTrueAndChef(user)).thenReturn(dishItemList);

        boolean isActive = dishItemService.isChefActive(user);
        Assertions.assertFalse(isActive);
    }

    @Test
    void isChefActive_ContainsUser_True() {
        UnregisteredUser user = new UnregisteredUser();
        Mockito.when(dishItemRepositoryMock.findAllByActiveIsTrueAndChef(user)).thenReturn(new ArrayList<>());

        boolean isActive = dishItemService.isChefActive(user);
        Assertions.assertTrue(isActive);
    }
}