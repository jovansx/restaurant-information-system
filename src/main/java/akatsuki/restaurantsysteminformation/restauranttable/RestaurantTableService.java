package akatsuki.restaurantsysteminformation.restauranttable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantTableService {
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public void setRestaurantTableRepository(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }
}
