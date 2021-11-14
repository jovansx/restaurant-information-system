package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemActionRequestDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsActiveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/dish-item")
@RequiredArgsConstructor
public class DishItemController {
    private final DishItemService dishItemService;

    @GetMapping("/active")
    public List<ItemsActiveDTO> getAllActive() {
        return this.dishItemService.getAllActive().stream().map(ItemsActiveDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DishItemDTO getOneActive(@PathVariable @Min(value = 1, message = "Id has to be a positive value.") long id) {
        return new DishItemDTO(this.dishItemService.getOneWithChef(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid DishItemCreateDTO dishItemCreateDTO) {
        dishItemService.create(dishItemCreateDTO);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid DishItemCreateDTO dishItemCreateDTO,
                       @PathVariable @Min(value = 1, message = "Id has to be a positive value.") long id) {
        dishItemService.update(dishItemCreateDTO, id);
    }

    @PutMapping("/change-state")
    public ItemsActiveDTO changeStateOfDishItem(@RequestBody @Valid DishItemActionRequestDTO dto) {
        return new ItemsActiveDTO(dishItemService.changeStateOfDishItems(dto.getItemId(), dto.getUserId()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(value = 1, message = "Id has to be a positive value.") long id) {
        dishItemService.delete(id);
    }
}
