package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.UpdateRestaurantTableDTO;
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
        // TODO proveri enume
        RestaurantTable table = Mapper.convertCreateRestaurantTableDTOToRestaurantTable(createRestaurantTableDTO);
        restaurantTableService.create(table);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody UpdateRestaurantTableDTO updateRestaurantTableDTO, @PathVariable long id) {
        // TODO proveri enume
        RestaurantTable table = Mapper.convertCreateRestaurantTableDTOToRestaurantTable(updateRestaurantTableDTO);
        restaurantTableService.update(table, id);
    }
}
