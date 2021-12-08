package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.itemcategory.CategoryType;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.order.exception.OrderDeletionException;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardException;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardNotActiveException;
import akatsuki.restaurantsysteminformation.order.exception.OrderNotFoundException;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepository orderRepositoryMock;

    @Mock
    RestaurantTableService restaurantTableServiceMock;

    @Mock
    UnregisteredUserService unregisteredUserServiceMock;

    @Mock
    DrinkItemsService drinkItemsServiceMock;

    @Mock
    ItemService itemServiceMock;

    @Test
    public void create_ValidDto_SavedObject() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO(1L, 1L);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.WAITER, false, "1111");

        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        orderService.create(orderCreateDTO);

        Mockito.verify(orderRepositoryMock, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    public void create_InvalidUserType_ExceptionThrown() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO(1L, 1L);
        UnregisteredUser user = new UnregisteredUser("Per", "Peri", "perperi@gmail.com",
                "0645678989", null, UserType.CHEF, false, "1111");

        Mockito.when(unregisteredUserServiceMock.getOne(1L)).thenReturn(user);

        Assertions.assertThrows(UserTypeNotValidException.class, () -> orderService.create(orderCreateDTO));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        UnregisteredUser user = new UnregisteredUser();
        Order order = new Order(1L, 500, LocalDateTime.now(), false,
                true, user, new ArrayList<>(), new ArrayList<>());

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        orderService.delete(1L);

        Mockito.verify(orderRepositoryMock, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void delete_OrderContainsItems_ExceptionThrown() {
        UnregisteredUser user = new UnregisteredUser();
        Order order = new Order(1L, 500, LocalDateTime.now(), false,
                true, user, Collections.singletonList(new DishItem()), new ArrayList<>());

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(OrderDeletionException.class, () -> orderService.delete(1L));
    }

    @Test
    public void updateTotalPriceAndSave_ValidOrder_SavedObject() {
        Item sandwich = new Item(1L, "1111-2222-3333", "Chicken sandwich", "Very good chicken sandwich!", null, true, false, ItemType.DISH, null, new ItemCategory("Meat", CategoryType.DISH), null);
        Item juice = new Item(2L, "2222-3333-4444", "Apple juice", "Very good apple juice!", null, true, false, ItemType.DRINK, null, new ItemCategory("Juice", CategoryType.DRINK), null);

        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.READY, true, 2, null, sandwich);
        DrinkItem drinkItem = new DrinkItem(2, juice);

        DrinkItems drinkItems = new DrinkItems(2L, "He want good apple joice!", LocalDateTime.now().minusMinutes(5),
                false, ItemState.PREPARATION, true, null, List.of(drinkItem));

        UnregisteredUser user = new UnregisteredUser();
        Order order = new Order(1L, 500, LocalDateTime.now(), false,
                true, user, Collections.singletonList(dishItem), List.of(drinkItems));

        Mockito.when(itemServiceMock.getCurrentPriceOfItem(1L)).thenReturn(350.0);
        Mockito.when(drinkItemsServiceMock.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(2L)).thenReturn(drinkItems);
        Mockito.when(itemServiceMock.getCurrentPriceOfItem(2L)).thenReturn(150.0);

        orderService.updateTotalPriceAndSave(order);

        Assertions.assertEquals(order.getTotalPrice(), 1000.0);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).save(order);
    }

    @Test
    public void charge_ValidId_SavedObject() {
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        orderService.charge(1L);

        Assertions.assertFalse(order.isActive());
        Assertions.assertFalse(order.getDishes().get(0).isActive());
        Assertions.assertFalse(order.getDrinks().get(0).isActive());
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).save(order);
    }

    @Test
    public void charge_OrderDiscarded_ExceptionThrown() {
        Order order = new Order(1L, 500, LocalDateTime.now(), true, true, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(OrderDiscardException.class, () -> orderService.charge(1L));
    }

    @Test
    public void charge_OrderNotActive_ExceptionThrown() {
        Order order = new Order(1L, 500, LocalDateTime.now(), false, false, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(OrderDiscardNotActiveException.class, () -> orderService.charge(1L));
    }

    @Test
    public void discard_ValidId_SavedObject() {
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        orderService.discard(1L);

        Assertions.assertTrue(order.isDiscarded());
        Assertions.assertFalse(order.isActive());
        Assertions.assertFalse(order.getDishes().get(0).isActive());
        Assertions.assertFalse(order.getDrinks().get(0).isActive());
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).save(order);
    }

    @Test
    public void discard_OrderDiscarded_ExceptionThrown() {
        Order order = new Order(1L, 500, LocalDateTime.now(), true, true, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(OrderDiscardException.class, () -> orderService.discard(1L));
    }

    @Test
    public void discard_OrderNotActive_ExceptionThrown() {
        Order order = new Order(1L, 500, LocalDateTime.now(), false, false, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        Assertions.assertThrows(OrderDiscardNotActiveException.class, () -> orderService.discard(1L));
    }

    @Test
    public void isWaiterActive_OrderContainsUser_ReturnedFalse() {
        UnregisteredUser user = new UnregisteredUser();
        List<Order> orders = List.of(new Order());

        Mockito.when(orderRepositoryMock.findAllByActiveIsTrueAndWaiter(user)).thenReturn(orders);

        boolean isActive = orderService.isWaiterActive(user);
        assertFalse(isActive);
    }

    @Test
    public void isWaiterActive_OrderNotContainsUser_ReturnedTrue() {
        UnregisteredUser user = new UnregisteredUser();
        List<Order> orders = new ArrayList<>();

        Mockito.when(orderRepositoryMock.findAllByActiveIsTrueAndWaiter(user)).thenReturn(orders);

        boolean isActive = orderService.isWaiterActive(user);
        assertTrue(isActive);
    }

    @Test
    public void getOneWithDishes_InvalidId_ExceptionThrown() {
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOneWithDishes(1L));
    }

    @Test
    public void getOneWithDrinks_InvalidId_ExceptionThrown() {
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOneWithDrinks(1L));
    }

    @Test
    public void getAllWithAll_FetchOrders_ReturnedList() {
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));
        Order order2 = new Order(2L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findAllIndexes()).thenReturn(List.of(1L, 2L));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(2L)).thenReturn(Optional.of(order2));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(2L)).thenReturn(Optional.of(order2));

        List<Order> orders = orderService.getAllWithAll();

        Assertions.assertEquals(orders.size(), 2);
    }

    @Test
    public void getAllActive_FetchActiveOrders_ReturnedList() {
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));
        Order order2 = new Order(2L, 500, LocalDateTime.now(), false, false, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findAllIndexes()).thenReturn(List.of(1L, 2L));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(2L)).thenReturn(Optional.of(order2));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(2L)).thenReturn(Optional.of(order2));

        List<Order> orders = orderService.getAllActive();

        Assertions.assertEquals(orders.size(), 1);
    }

    @Test
    public void getOneByRestaurantTableId_ValidId_ReturnedObject() {
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(restaurantTableServiceMock.getActiveOrderIdByTableId(1L)).thenReturn(1L);
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOneByRestaurantTableId(1L);

        Assertions.assertEquals(foundOrder, order);
    }

    @Test
    public void getOneByOrderItem_DishItem_ReturnedObject() {
        DishItem dishItem = new DishItem(1L, "Old note.", LocalDateTime.now(), false, ItemState.READY, true, 2, null, null);
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));
        Order order2 = new Order(2L, 500, LocalDateTime.now(), false, false, null, List.of(dishItem), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findAllIndexes()).thenReturn(List.of(1L, 2L));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(2L)).thenReturn(Optional.of(order2));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(2L)).thenReturn(Optional.of(order2));

        Order foundOrder = orderService.getOneByOrderItem(dishItem);

        Assertions.assertEquals(foundOrder, order2);
    }

    @Test
    public void getOneByOrderItem_DrinkItems_ReturnedObject() {
        DrinkItems drinkItems = new DrinkItems(2L, "He want good apple joice!", LocalDateTime.now().minusMinutes(5),
                false, ItemState.PREPARATION, true, null, null);
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));
        Order order2 = new Order(2L, 500, LocalDateTime.now(), false, false, null, List.of(new DishItem()), List.of(drinkItems));

        Mockito.when(orderRepositoryMock.findAllIndexes()).thenReturn(List.of(1L, 2L));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(2L)).thenReturn(Optional.of(order2));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(2L)).thenReturn(Optional.of(order2));

        Order foundOrder = orderService.getOneByOrderItem(drinkItems);

        Assertions.assertEquals(foundOrder, order2);
    }

    @Test
    public void getOneByOrderItem_DrinkItems_ExceptionThrown() {
        DrinkItems drinkItems = new DrinkItems(2L, "He want good apple joice!", LocalDateTime.now().minusMinutes(5),
                false, ItemState.PREPARATION, true, null, null);
        Order order = new Order(1L, 500, LocalDateTime.now(), false, true, null, List.of(new DishItem()), List.of(new DrinkItems()));
        Order order2 = new Order(2L, 500, LocalDateTime.now(), false, false, null, List.of(new DishItem()), List.of(new DrinkItems()));

        Mockito.when(orderRepositoryMock.findAllIndexes()).thenReturn(List.of(1L, 2L));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndDrinks(2L)).thenReturn(Optional.of(order2));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(1L)).thenReturn(Optional.of(order));
        Mockito.when(orderRepositoryMock.findByIdAndFetchWaiterAndFetchDishesAndItems(2L)).thenReturn(Optional.of(order2));

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOneByOrderItem(drinkItems));
    }
}