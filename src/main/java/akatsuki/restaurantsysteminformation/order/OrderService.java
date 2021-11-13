package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getOne(long id);

//    void create(OrderCreateDTO order);

    void discard(long id);

    void charge(long id);

    void addDishItemToOrder(DishItem dishItem, Order order);
}
