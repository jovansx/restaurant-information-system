package akatsuki.restaurantsysteminformation.restauranttable;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTable getOne(long tableId);

    RestaurantTable getOneWithOrder(long tableId);

    List<RestaurantTable> getAll();

    RestaurantTable create(RestaurantTable restaurantTable, long roomId);

    RestaurantTable update(RestaurantTable table, long id, long roomId);

    RestaurantTable delete(long id);

    Long getActiveOrderIdByTableId(long id);
}
