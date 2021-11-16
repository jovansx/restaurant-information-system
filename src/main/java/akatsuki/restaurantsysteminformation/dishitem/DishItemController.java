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
import javax.validation.constraints.Positive;
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
    public DishItemDTO getOneActive(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new DishItemDTO(this.dishItemService.findOneActiveAndFetchItemAndChef(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid DishItemCreateDTO dishItemCreateDTO) {
        dishItemService.create(dishItemCreateDTO);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid DishItemCreateDTO dishItemCreateDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        dishItemService.update(dishItemCreateDTO, id);
    }

    @PutMapping("/prepare/{id}")
    public void prepare(@PathVariable @Positive(message = "Id has to be a positive value.") long id,
                        @RequestParam("waiter_id") @Positive(message = "Waiter id has to be a positive value.") long waiterId) {
        dishItemService.prepare(id, waiterId);
    }

    @PutMapping("/change-state")
    public ItemsActiveDTO changeStateOfDishItem(@RequestBody @Valid DishItemActionRequestDTO dto) {
        return new ItemsActiveDTO(dishItemService.changeStateOfDishItems(dto.getItemId(), dto.getUserId()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        dishItemService.delete(id);
    }
}
