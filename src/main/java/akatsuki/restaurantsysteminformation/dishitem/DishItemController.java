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

    @GetMapping("/{id}")
    public DishItemDTO getOneActive(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new DishItemDTO(this.dishItemService.findOneActiveAndFetchItemAndChef(id));
    }

    @GetMapping("/active")
    public List<ItemsActiveDTO> findAllActiveAndStateIsNotNewOrDelivered() {
        return this.dishItemService.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered().stream().map(ItemsActiveDTO::new).collect(Collectors.toList());
    }

}
