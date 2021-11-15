package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.order.Order;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTable create(RestaurantTable restaurantTable);

    List<RestaurantTable> getAll();

    RestaurantTable update(RestaurantTable table, long id);

    void delete(long id);

    RestaurantTable getOne(long tableId);

    RestaurantTable getOneWithOrder(long tableId);

    Order getActiveOrderByTableId(long id);
}
