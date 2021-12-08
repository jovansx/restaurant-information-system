package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTable getOne(long tableId);

    RestaurantTable getOneWithOrder(long tableId);

    List<RestaurantTable> getAll();

    RestaurantTable create(RestaurantTable restaurantTable, long roomId);

    RestaurantTable update(RestaurantTable table, long id, long roomId);

    void changeStateOfTableWithOrder(Order order, TableState state);

    RestaurantTable delete(long id);

    Long getActiveOrderIdByTableId(long id);

    Long getOrderByTableName(String name);

    RestaurantTable getOneByNameWithOrder(String name);

    void setOrderToTable(Long tableId, Order order);
}
