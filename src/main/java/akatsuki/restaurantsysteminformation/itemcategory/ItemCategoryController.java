package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/item-category")
@RequiredArgsConstructor
@Validated
public class ItemCategoryController {
    private final ItemCategoryService itemCategoryService;

    @GetMapping("/{id}")
    public ItemCategoryDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new ItemCategoryDTO(itemCategoryService.getOne(id));
    }

    @GetMapping
    public List<ItemCategoryDTO> getAll() {
        return itemCategoryService.getAll().stream().map(ItemCategoryDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/drink")
    public List<ItemCategory> getAllDrinkCategories() {
        return itemCategoryService.getAll().stream().filter(i -> i.getType().equals(ItemType.DRINK)).collect(Collectors.toList());
    }

    @GetMapping("/dish")
    public List<ItemCategory> getAllDishCategories() {
        return itemCategoryService.getAll().stream().filter(i -> i.getType().equals(ItemType.DISH)).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid ItemCategoryDTO dto) {
        return itemCategoryService.create(new ItemCategory(dto.getName(), dto.getType())).getId().toString();
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid ItemCategoryDTO dto,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemCategoryService.update(new ItemCategory(dto.getName(), dto.getType()), id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemCategoryService.delete(id);
    }

}
