package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.order.exception.OrderDeletionException;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardException;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardNotActiveException;
import akatsuki.restaurantsysteminformation.order.exception.OrderNotFoundException;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceIntegrationTest {

    @Autowired
    OrderServiceImpl orderService;

    @Test
    public void create_ValidDto_SavedObject() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO(1L);
        Order order = orderService.create(orderCreateDTO);
        Assertions.assertNotNull(order);
    }

    @Test
    public void create_InvalidUserType_ExceptionThrown() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO(2L);
        Assertions.assertThrows(UserTypeNotValidException.class, () -> orderService.create(orderCreateDTO));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        Order deletedOrder = orderService.delete(2L);
        Assertions.assertNotNull(deletedOrder);
    }

    @Test
    public void delete_OrderContainsItems_ExceptionThrown() {
        Assertions.assertThrows(OrderDeletionException.class, () -> orderService.delete(1L));
    }

    @Test
    public void updateTotalPriceAndSave_ValidOrder_SavedObject() {

        Item sandwich = new Item();
        sandwich.setId(4L);

        DishItem dishItem = new DishItem();
        dishItem.setId(1L);
        dishItem.setItem(sandwich);

        DrinkItems drinkItems = new DrinkItems();
        drinkItems.setId(6L);

        Order order = new Order(1L, 500, LocalDateTime.now(), false,
                true, null, Collections.singletonList(dishItem), List.of(drinkItems));

        Order updatedOrder = orderService.updateTotalPriceAndSave(order);

        Assertions.assertEquals(24, updatedOrder.getTotalPrice());
    }

    @Test
    public void charge_ValidId_SavedObject() {
        Order chargedOrder = orderService.charge(1L);

        Assertions.assertFalse(chargedOrder.isActive());
        Assertions.assertFalse(chargedOrder.getDishes().get(0).isActive());
        Assertions.assertFalse(chargedOrder.getDrinks().get(0).isActive());
    }

    @Test
    public void charge_OrderDiscarded_ExceptionThrown() {
        Assertions.assertThrows(OrderDiscardException.class, () -> orderService.charge(4L));
    }

    @Test
    public void charge_OrderNotActive_ExceptionThrown() {
        Assertions.assertThrows(OrderDiscardNotActiveException.class, () -> orderService.charge(5L));
    }

    @Test
    public void discard_ValidId_SavedObject() {
        Order discardedOrder = orderService.discard(1L);

        Assertions.assertTrue(discardedOrder.isDiscarded());
        Assertions.assertFalse(discardedOrder.isActive());
        Assertions.assertFalse(discardedOrder.getDishes().get(0).isActive());
        Assertions.assertFalse(discardedOrder.getDrinks().get(0).isActive());
    }

    @Test
    public void discard_OrderDiscarded_ExceptionThrown() {
        Assertions.assertThrows(OrderDiscardException.class, () -> orderService.discard(4L));
    }

    @Test
    public void discard_OrderNotActive_ExceptionThrown() {
        Assertions.assertThrows(OrderDiscardNotActiveException.class, () -> orderService.discard(5L));
    }

    @Test
    public void isWaiterActive_OrderContainsUser_ReturnedFalse() {
        UnregisteredUser user = new UnregisteredUser();
        user.setId(1L);

        boolean isActive = orderService.isWaiterActive(user);
        assertFalse(isActive);
    }

    @Test
    public void isWaiterActive_OrderNotContainsUser_ReturnedTrue() {
        UnregisteredUser user = new UnregisteredUser();
        user.setId(4L);

        boolean isActive = orderService.isWaiterActive(user);
        assertTrue(isActive);
    }

    @Test
    public void getOneWithDishes_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOneWithDishes(1000L));
    }

    @Test
    public void getOneWithDrinks_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOneWithDrinks(1000L));
    }

    @Test
    public void getAllWithAll_FetchOrders_ReturnedList() {
        List<Order> orders = orderService.getAllWithAll();
        Assertions.assertEquals(10, orders.size());
    }

    @Test
    public void getAllActive_FetchActiveOrders_ReturnedList() {
        List<Order> orders = orderService.getAllActive();
        Assertions.assertEquals(8, orders.size());
    }

    @Test
    public void getOneByRestaurantTableId_ValidId_ReturnedObject() {
        Order foundOrder = orderService.getOneByRestaurantTableId(1L);
        Assertions.assertNotNull(foundOrder);
    }

    @Test
    public void getOneByOrderItem_DishItem_ReturnedObject() {
        DishItem dishItem = new DishItem();
        dishItem.setId(1L);

        Order foundOrder = orderService.getOneByOrderItem(dishItem);

        Assertions.assertNotNull(foundOrder);
    }

    @Test
    public void getOneByOrderItem_DrinkItems_ReturnedObject() {
        DrinkItems drinkItems = new DrinkItems();
        drinkItems.setId(6L);

        Order foundOrder = orderService.getOneByOrderItem(drinkItems);

        Assertions.assertNotNull(foundOrder);
    }

    @Test
    public void getOneByOrderItem_DrinkItems_ExceptionThrown() {
        DrinkItems drinkItems = new DrinkItems();
        drinkItems.setId(60L);

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOneByOrderItem(drinkItems));
    }
}