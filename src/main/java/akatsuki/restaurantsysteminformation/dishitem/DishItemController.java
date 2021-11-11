package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemDTOActionRequest;
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
    public List<ItemsDTOActive> getAllActiveDrinkItems() {
        return this.dishItemService.getAllActive().stream().map(ItemsDTOActive::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DishItemDTO getOne(@PathVariable long id) {
        return new DishItemDTO(this.dishItemService.getOne(id));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemsDTOActive changeStateOfDishItem(@RequestBody DishItemDTOActionRequest dto) {
        return new ItemsDTOActive(dishItemService.changeStateOfDishItems(dto.getItemId(), dto.getUserId()));
    }


}
