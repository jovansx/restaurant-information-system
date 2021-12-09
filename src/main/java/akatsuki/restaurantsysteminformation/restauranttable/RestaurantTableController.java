package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurant-table")
@RequiredArgsConstructor
@Validated
public class RestaurantTableController {
    private final RestaurantTableService restaurantTableService;

    //  TODO proveri da li se koristi
    @GetMapping("/{id}")
    public RestaurantTableDTO getOne(@PathVariable("id") @Positive(message = "Id has to be a positive value.") long id) {
        return new RestaurantTableDTO(restaurantTableService.getOne(id));
    }

    //  TODO proveri da li se koristi
    @GetMapping
    public List<RestaurantTableDTO> getAll() {
        List<RestaurantTable> tables = restaurantTableService.getAll();
        return tables.stream().map(RestaurantTableDTO::new).collect(Collectors.toList());
    }

}