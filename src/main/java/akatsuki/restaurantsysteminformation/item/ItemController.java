package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.item.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/not-active/{id}")
    public ItemDetailsDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new ItemDetailsDTO(itemService.getOne(id));
    }

    //    TODO NE KORISTI SE KOD MENE
    @GetMapping("/{id}")
    public ItemDetailsDTO getOneActive(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new ItemDetailsDTO(itemService.getOneActive(id));
    }

    //    TODO NE KORISTI SE KOD MENE
    @GetMapping
    public List<ItemDetailsDTO> getAllActive() {
        return itemService.getAllActive().stream().map(ItemDetailsDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/menu")
    public List<ItemBasicInfoDTO> getAllForMenuInsight() {
        return itemService.getAllForMenuInsight().stream().map(ItemBasicInfoDTO::new).collect(Collectors.toList());
    }

    //    TODO Ovo koristi jelena
    @GetMapping("/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemForMenuDTO> getAllByCategory(@PathVariable @NotEmpty(message = "It cannot be empty.") String category) {
        return itemService.getAllActiveByCategory(category).stream().map(ItemForMenuDTO::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid ItemCreateDTO itemDTO) {
        return itemService.create(new Item(itemDTO)).getId().toString();
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @PutMapping("/{id}")
    public String update(@RequestBody @Valid ItemUpdateDTO itemDTO,
                         @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return itemService.update(new Item(itemDTO), id).getId().toString();
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return itemService.delete(id).getId().toString();
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @PostMapping("/save-changes")
    public void saveChanges() {
        this.itemService.saveChanges();
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @PostMapping("/discard-changes")
    public void discardChanges() {
        this.itemService.discardChanges();
    }
}
