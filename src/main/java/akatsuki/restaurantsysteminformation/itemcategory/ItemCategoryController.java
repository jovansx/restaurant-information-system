package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/item-category")
public class ItemCategoryController {
    private final ItemCategoryService itemCategoryService;

    @Autowired
    public ItemCategoryController(ItemCategoryService itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemCategoryDTO getOne(@PathVariable long id) {
        return new ItemCategoryDTO(itemCategoryService.getOne(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemCategoryDTO> getAll() {
        return itemCategoryService.getAll().stream().map(ItemCategoryDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ItemCategoryDTO dto) {
        itemCategoryService.create(new ItemCategory(dto.getName()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody ItemCategoryDTO dto, @PathVariable long id) {
        itemCategoryService.update(new ItemCategory(dto.getName()), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        itemCategoryService.delete(id);
    }
}
