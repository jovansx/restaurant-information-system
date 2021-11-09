package akatsuki.restaurantsysteminformation.restauranttable;

import java.util.List;

public interface RestaurantTableService {
    List<RestaurantTable> getTablesFromIds(List<Long> tableIds);
    void create(RestaurantTable restaurantTable);

    List<RestaurantTable> findAll();
}
