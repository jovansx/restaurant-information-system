package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderDTO;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.util.List;

public interface OrderService {

    Order getOneByOrderItem(OrderItem orderItem);

    Order getOneWithAll(Long orderId);

    Order getOneWithDishes(Long orderId);

    Order getOneWithDrinks(Long orderId);

    Order getOneByRestaurantTableId(long id);

    List<Order> getAllWithAll();

    List<Order> getAllActive();

    Order create(OrderCreateDTO order);

    Order delete(long id);

    Order updateTotalPriceAndSave(Order order);

    Order charge(long id);

    Order discard(long id);

    boolean isWaiterActive(UnregisteredUser user);

    void save(Order order);

    OrderDTO getOrderByRestaurantTableNameIfWaiterValid(String name, String pinCode);
}
