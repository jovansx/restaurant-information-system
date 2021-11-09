package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTOActionRequest;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTOActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drink-items")
public class DrinkItemsController {
    private final DrinkItemsService drinkItemsService;

    @Autowired
    public DrinkItemsController(DrinkItemsService drinkItemsService) {
        this.drinkItemsService = drinkItemsService;
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<DrinkItemsDTOActive> getAllActiveDrinkItems() {
        return this.drinkItemsService.getAllActive().stream().map(DrinkItemsDTOActive::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DrinkItemsDTO getOne(@PathVariable long id) {
        return new DrinkItemsDTO(this.drinkItemsService.getOne(id));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DrinkItemsDTOActive changeStateOfDrinkItems(@RequestBody DrinkItemsDTOActionRequest dto) {
        DrinkItems drinkItems = drinkItemsService.changeStateOfDrinkItems(dto.getItemId(), dto.getUserId());
        return new DrinkItemsDTOActive(drinkItems);
    }

}
