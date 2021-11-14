package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.item.dto.ItemDTO;
import akatsuki.restaurantsysteminformation.item.dto.ItemDTOCreate;
import akatsuki.restaurantsysteminformation.item.dto.ItemDTOForMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getAll() {
        return itemService.getAll().stream().map(ItemDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTOForMenu> getAllByCategory(@PathVariable String category) {
        return itemService.getAllByCategory(category).stream().map(ItemDTOForMenu::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getOne(@PathVariable long id) {
        return new ItemDTO(itemService.getOne(id));
    }

    @PostMapping("/save-changes")
    @ResponseStatus(HttpStatus.OK)
    public void saveChanges() {
        this.itemService.saveChanges();
    }

    @PostMapping("/discard-changes")
    @ResponseStatus(HttpStatus.OK)
    public void discardChanges() {
        this.itemService.discardChanges();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ItemDTOCreate itemDTO) {
        itemService.create(new Item(itemDTO));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody ItemDTOCreate itemDTO, @PathVariable long id) {
        itemService.update(new Item(itemDTO), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        itemService.delete(id);
    }
}
