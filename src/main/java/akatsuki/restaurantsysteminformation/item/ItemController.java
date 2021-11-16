package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.item.dto.ItemCreateDTO;
import akatsuki.restaurantsysteminformation.item.dto.ItemForMenuDTO;
import akatsuki.restaurantsysteminformation.item.dto.ItemDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDetailsDTO> getAll() {
        return itemService.getAll().stream().map(ItemDetailsDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemForMenuDTO> getAllByCategory(@PathVariable @NotEmpty(message = "It cannot be empty.") String category) {
        return itemService.getAllByCategory(category).stream().map(ItemForMenuDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemDetailsDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new ItemDetailsDTO(itemService.getOne(id));
    }

    @PostMapping("/save-changes")
    public void saveChanges() {
        this.itemService.saveChanges();
    }

    @PostMapping("/discard-changes")
    public void discardChanges() {
        this.itemService.discardChanges();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ItemCreateDTO itemDTO) {
        itemService.create(new Item(itemDTO));
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid ItemCreateDTO itemDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemService.update(new Item(itemDTO), id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        itemService.delete(id);
    }
}
