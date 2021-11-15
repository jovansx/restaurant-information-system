package akatsuki.restaurantsysteminformation.itemcategory;

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
    private final ItemCategoryServiceImpl itemCategoryService;

    @GetMapping("/{id}")
    public ItemCategoryDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new ItemCategoryDTO(itemCategoryService.getOne(id));
    }

    @GetMapping
    public List<ItemCategoryDTO> getAll() {
        return itemCategoryService.getAll().stream().map(ItemCategoryDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ItemCategoryDTO dto) {
        itemCategoryService.create(new ItemCategory(dto.getName()));
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid ItemCategoryDTO dto,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemCategoryService.update(new ItemCategory(dto.getName()), id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemCategoryService.delete(id);
    }
}
