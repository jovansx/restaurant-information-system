package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsActionRequestDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsActiveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drink-items")
@RequiredArgsConstructor
@Validated
public class DrinkItemsController {
    private final DrinkItemsService drinkItemsService;

    @GetMapping("/{id}")
    public DrinkItemsDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new DrinkItemsDTO(this.drinkItemsService.getOne(id));
    }

    @GetMapping
    public List<ItemsActiveDTO> getAll() {
        return this.drinkItemsService.getAll().stream().map(ItemsActiveDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/active/{id}")
    public DrinkItemsDTO getOneActive(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new DrinkItemsDTO(this.drinkItemsService.getOneActive(id));
    }

    @GetMapping("/active")
    public List<ItemsActiveDTO> getAllActiveDrinkItems() {
        return this.drinkItemsService.getAllActive().stream().map(ItemsActiveDTO::new).collect(Collectors.toList());
    }

    @PutMapping("/change-state")
    public ItemsActiveDTO changeStateOfDrinkItems(@RequestBody @Valid DrinkItemsActionRequestDTO dto) {
        return new ItemsActiveDTO(drinkItemsService.changeStateOfDrinkItems(dto.getItemId(), dto.getUserId()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        drinkItemsService.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid DrinkItemsCreateDTO drinkItemsDTO) {
        drinkItemsService.create(drinkItemsDTO);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody DrinkItemsCreateDTO drinkItemsDTO,
                       @PathVariable @Min(value = 1, message = "Id has to be a positive value.") long id) {
        drinkItemsService.update(drinkItemsDTO, id);
    }
}
