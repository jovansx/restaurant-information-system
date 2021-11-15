package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    List<Order> getAllActive();

    Order getOne(long id);

    void create(OrderCreateDTO order);

    void updateTotalPriceAndSave(Order order);

    void discard(long id);

    void charge(long id);

    void delete(long id);

    Order getOrderByOrderItem(OrderItem orderItem);

    Order getOneWithDishes(Long orderId);

    Order getOneWithDrinks(Long orderId);

    Order getOneByRestaurantTable(long id);

    boolean isWaiterActive(UnregisteredUser user);
}
