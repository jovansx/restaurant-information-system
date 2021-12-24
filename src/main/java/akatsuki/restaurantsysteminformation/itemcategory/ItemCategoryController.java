package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @GetMapping
    public List<ItemCategoryDTO> getAll() {
        return itemCategoryService.getAll().stream().map(ItemCategoryDTO::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @GetMapping("/drink")
    public List<ItemCategory> getAllDrinkCategories() {
        return itemCategoryService.getAll().stream().filter(i -> i.getType().equals(ItemType.DRINK)).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @GetMapping("/dish")
    public List<ItemCategory> getAllDishCategories() {
        return itemCategoryService.getAll().stream().filter(i -> i.getType().equals(ItemType.DISH)).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid ItemCategoryDTO dto) {
        return itemCategoryService.create(new ItemCategory(dto.getName(), dto.getType())).getId().toString();
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemCategoryService.delete(id);
    }

}
