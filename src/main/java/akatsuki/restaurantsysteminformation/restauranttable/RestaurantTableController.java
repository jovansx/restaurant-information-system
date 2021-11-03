package akatsuki.restaurantsysteminformation.restauranttable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("restaurant-table")
public class RestaurantTableController {
    private RestaurantTableServiceImpl restaurantTableService;

    @Autowired
    public RestaurantTableController(RestaurantTableServiceImpl restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }
}
