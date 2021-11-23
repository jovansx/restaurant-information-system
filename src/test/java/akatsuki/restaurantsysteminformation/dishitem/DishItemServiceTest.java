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
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderServiceImpl;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserServiceImpl;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("When invalid id is passed, exception should occur.")
    public void findOneActiveAndFetchItemAndChef_NegativeId_ExceptionThrown() {
        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChef(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DishItemNotFoundException.class, () -> {
            dishItemService.findOneActiveAndFetchItemAndChef(8000L);
        });
    }

    @Test
    @DisplayName("When valid id is passed, required object is returned.")
    public void findOneActiveAndFetchItemAndChef_ValidId_ReturnedObject() {
        DishItem dishItem = new DishItem();

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChef(1L)).thenReturn(Optional.of(dishItem));
        DishItem foundDishItem = dishItemService.findOneActiveAndFetchItemAndChef(1L);

        Assertions.assertEquals(foundDishItem, dishItem);
    }

    @Test
    @DisplayName("When there are objects in the database, return list.")
    void findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered_DishItemsExist_ReturnedList() {
        List<DishItem> list = Collections.singletonList(new DishItem());
        Mockito.when(dishItemRepositoryMock.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered()).thenReturn(Optional.of(list));

        List<DishItem> foundList = dishItemService.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();
        Assertions.assertEquals(foundList, list);
    }

    @Test
    @DisplayName("When there are not objects in the database, exception should occur.")
    void findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered_DishItemsDontExist_ExceptionThrown() {
        List<DishItem> list = Collections.singletonList(new DishItem());
        Mockito.when(dishItemRepositoryMock.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered()).thenReturn(Optional.of(list));

        List<DishItem> foundList = dishItemService.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();
        Assertions.assertEquals(foundList, list);
    }

    @Test
    @DisplayName("When valid dto is passed, new object is created.")
    public void create_ValidDto_SavedObject() {
        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(1L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        dishItemService.create(dishItemCreateDTO);

        Assertions.assertEquals(order.getDishes().size(), 1);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(Mockito.any(DishItem.class));
        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
    }

    @Test
    @DisplayName("When invalid item type is passed, exception should occur.")
    public void create_InvalidItemType_ExceptionThrown() {
        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(1L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemInvalidTypeException.class, () -> dishItemService.create(dishItemCreateDTO));
    }

    @Test
    @DisplayName("When valid dto and id are passed, required object is changed.")
    public void update_ValidDtoAndId_ChangedObject() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());
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
    @DisplayName("When invalid type is passed, exception should occur.")
    public void update_InvalidItemType_ExceptionThrown() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemInvalidTypeException.class, () -> dishItemService.update(dto, 1L));
    }

    @Test
    @DisplayName("When valid dto and id are passed, exception should occur.")
    public void update_InvalidDishItem_ExceptionThrown() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());
        DishItem dishItem = new DishItem("Old note.", LocalDateTime.now(), false, ItemState.NEW, 5, null, null, true);
        dishItem.setId(1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemOrderException.class, () -> dishItemService.update(dto, 1L));
    }

    @Test
    @DisplayName("When invalid item state is passed, exception should occur.")
    public void update_InvalidItemState_ExceptionThrown() {
        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());
        DishItem dishItem = new DishItem("Old note.", LocalDateTime.now(), false, ItemState.PREPARATION, 5, null, null, true);
        dishItem.setId(1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, Collections.singletonList(dishItem), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemInvalidStateException.class, () -> dishItemService.update(dto, 1L));
    }

    @Test
    @DisplayName("When valid ids are passed, the state is changed from new to on hold.")
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
    @DisplayName("When valid ids are passed, the state is changed from ready to delivered.")
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
    @DisplayName("When valid ids are passed, the state is changed from on hold to preparation.")
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
    @DisplayName("When valid ids are passed, the state is changed from preparation to ready.")
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
    @DisplayName("When invalid item id is passed, exception should occur.")
    void changeStateOfDishItems_InvalidItemId_ExceptionThrown() {
        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DishItemNotFoundException.class, () -> dishItemService.changeStateOfDishItems(1000L, 1L));
    }

    @Test
    @DisplayName("When invalid item state is passed, exception should occur.")
    void changeStateOfDishItems_InvalidItemState_ExceptionThrown() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.DELIVERED, true, 5, null, null);
        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1L)).thenReturn(Optional.of(dishItem));
        Assertions.assertThrows(DishItemInvalidStateException.class, () -> dishItemService.changeStateOfDishItems(1L, 1L));
    }

    @Test
    @DisplayName("When not allowed user change state, exception should occur.")
    void changeStateOfDishItems_NotAllowedUserType_ExceptionThrown() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.NEW, true, 5, null, null);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.CHEF, false, "1111");

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(1L)).thenReturn(Optional.of(dishItem));
        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        Assertions.assertThrows(UserTypeNotValidException.class, () -> dishItemService.changeStateOfDishItems(1L, 1L));
    }

    @Test
    @DisplayName("When valid id is passed, required object is deleted.")
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
    @DisplayName("When invalid id is passed, exception should occur.")
    public void delete_InvalidId_ExceptionThrown() {
        Mockito.when(dishItemRepositoryMock.findByIdAndActiveIsTrue(1000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DishItemNotFoundException.class, () -> dishItemService.delete(1000L));
    }

    @Test
    @DisplayName("When valid unregistered user is passed, return false.")
    void isChefActive_ContainsUser_False() {
        UnregisteredUser user = new UnregisteredUser();
        List<DishItem> dishItemList = Collections.singletonList(new DishItem());

        Mockito.when(dishItemRepositoryMock.findAllByActiveIsTrueAndChef(user)).thenReturn(dishItemList);

        boolean isActive = dishItemService.isChefActive(user);
        Assertions.assertFalse(isActive);
    }

    @Test
    @DisplayName("When valid unregistered user is passed, return true.")
    void isChefActive_ContainsUser_True() {
        UnregisteredUser user = new UnregisteredUser();
        Mockito.when(dishItemRepositoryMock.findAllByActiveIsTrueAndChef(user)).thenReturn(new ArrayList<>());

        boolean isActive = dishItemService.isChefActive(user);
        Assertions.assertTrue(isActive);
    }
}