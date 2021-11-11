package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableRepresentationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant-table")
public class RestaurantTableController {
    private final RestaurantTableService restaurantTableService;

    @Autowired
    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RestaurantTableRepresentationDTO> getAll() {
        List<RestaurantTable> tables = restaurantTableService.getAll();
        List<RestaurantTableRepresentationDTO> tablesDTO = new ArrayList<>();
        tables.forEach(table -> {
            tablesDTO.add(new RestaurantTableRepresentationDTO(table));
        });
        return tablesDTO;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestaurantTableRepresentationDTO getOne(@PathVariable("id") long id) {
        RestaurantTable table = restaurantTableService.getOne(id);
        return new RestaurantTableRepresentationDTO(table);
    }
}
