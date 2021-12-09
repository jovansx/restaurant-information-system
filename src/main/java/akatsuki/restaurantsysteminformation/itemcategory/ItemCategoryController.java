package akatsuki.restaurantsysteminformation.itemcategory;

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

    //    TODO vidi kod simica jel mu treba
    @GetMapping("/{id}")
    public ItemCategoryDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new ItemCategoryDTO(itemCategoryService.getOne(id));
    }

    @GetMapping
    public List<ItemCategoryDTO> getAll() {
        return itemCategoryService.getAll().stream().map(ItemCategoryDTO::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid ItemCategoryDTO dto) {
        return itemCategoryService.create(new ItemCategory(dto.getName(), dto.getType())).getId().toString();
    }

    //    TODO vidi kod simica jel mu treba
    @PutMapping("/{id}")
    public void update(@RequestBody @Valid ItemCategoryDTO dto,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemCategoryService.update(new ItemCategory(dto.getName(), dto.getType()), id);
    }

    //    TODO vidi kod simica jel mu treba
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemCategoryService.delete(id);
    }

}
