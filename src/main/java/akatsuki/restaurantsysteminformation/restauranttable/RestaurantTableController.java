package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant-table")
public class RestaurantTableController {
    private final RestaurantTableService restaurantTableService;

    @Autowired
    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateRestaurantTableDTO createRestaurantTableDTO) {
        RestaurantTable table = Mapper.convertCreateRestaurantTableDTOToRestaurantTable(createRestaurantTableDTO);
        restaurantTableService.create(table);
    }
}
