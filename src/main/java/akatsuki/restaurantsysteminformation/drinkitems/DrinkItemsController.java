package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsActiveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drink-items")
@RequiredArgsConstructor
@Validated
public class DrinkItemsController {
    private final DrinkItemsService drinkItemsService;

    @GetMapping("/active/{id}")
    public DrinkItemsDTO getOneActive(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new DrinkItemsDTO(this.drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(id));
    }

    @GetMapping("/active")
    public List<ItemsActiveDTO> getAllActiveDrinkItems() {
        return this.drinkItemsService.findAllActiveAndFetchBartenderAndItems().stream().map(ItemsActiveDTO::new).collect(Collectors.toList());
    }

}
