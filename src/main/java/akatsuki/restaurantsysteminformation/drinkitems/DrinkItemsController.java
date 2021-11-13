package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTOActionRequest;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsDTOActive;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DrinkItemsDTO getOne(@PathVariable long id) {
        return new DrinkItemsDTO(this.drinkItemsService.getOne(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsDTOActive> getAll() {
        return this.drinkItemsService.getAll().stream().map(ItemsDTOActive::new).collect(Collectors.toList());
    }

    @GetMapping("/active/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DrinkItemsDTO getOneActive(@PathVariable long id) {
        return new DrinkItemsDTO(this.drinkItemsService.getOneActive(id));
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsDTOActive> getAllActiveDrinkItems() {
        return this.drinkItemsService.getAllActive().stream().map(ItemsDTOActive::new).collect(Collectors.toList());
    }

    @PutMapping("/change-state")
    @ResponseStatus(HttpStatus.OK)
    public ItemsDTOActive changeStateOfDrinkItems(@RequestBody DrinkItemsDTOActionRequest dto) {
        return new ItemsDTOActive(drinkItemsService.changeStateOfDrinkItems(dto.getItemId(), dto.getUserId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        drinkItemsService.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody DrinkItemsCreateDTO drinkItemsDTO) {
        drinkItemsService.create(drinkItemsDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody DrinkItemsCreateDTO drinkItemsDTO, @PathVariable long id) {
        drinkItemsService.update(drinkItemsDTO, id);
    }
}
