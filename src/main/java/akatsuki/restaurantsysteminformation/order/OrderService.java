package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getOne(long id);

    void create(OrderCreateDTO order);

    void updateTotalPrice(Order order);

    void discard(long id);

    void charge(long id);

    void delete(long id);

    void addDrinkItemsToCollection(DrinkItems drinkItems, Order order);

}
