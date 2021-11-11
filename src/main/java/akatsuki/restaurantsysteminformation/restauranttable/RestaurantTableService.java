package akatsuki.restaurantsysteminformation.restauranttable;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTable create(RestaurantTable restaurantTable);
    List<RestaurantTable> findAll();
    RestaurantTable update(RestaurantTable table, long id);
    void delete(long id);
    RestaurantTable getOne(long tableId);
}
