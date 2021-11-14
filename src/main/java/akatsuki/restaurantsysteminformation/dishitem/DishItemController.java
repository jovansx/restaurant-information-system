package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemActionRequestDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsDTOActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dish-item")
public class DishItemController {
    private final DishItemService dishItemService;

    @Autowired
    public DishItemController(DishItemService dishItemService) {
        this.dishItemService = dishItemService;
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemsDTOActive> getAllActive() {
        return this.dishItemService.getAllActive().stream().map(ItemsDTOActive::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DishItemDTO getOneActive(@PathVariable long id) {
        return new DishItemDTO(this.dishItemService.getOneWithChef(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody DishItemCreateDTO dishItemCreateDTO) {
        dishItemService.create(dishItemCreateDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody DishItemCreateDTO dishItemCreateDTO, @PathVariable long id) {
        dishItemService.update(dishItemCreateDTO, id);
    }

    @PutMapping("/change-state")
    @ResponseStatus(HttpStatus.OK)
    public ItemsDTOActive changeStateOfDishItem(@RequestBody DishItemActionRequestDTO dto) {
        return new ItemsDTOActive(dishItemService.changeStateOfDishItems(dto.getItemId(), dto.getUserId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        dishItemService.delete(id);
    }

}
