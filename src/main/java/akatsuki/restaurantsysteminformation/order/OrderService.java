package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
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

    void create(OrderCreateDTO order);

    void delete(long id);

    void updateTotalPriceAndSave(Order order);

    void charge(long id);

    void discard(long id);

    boolean isWaiterActive(UnregisteredUser user);

}
