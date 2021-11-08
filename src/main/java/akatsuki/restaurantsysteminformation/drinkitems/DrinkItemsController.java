package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTOActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/drink-items")
public class DrinkItemsController {
    private final DrinkItemsService drinkItemsService;

    @Autowired
    public DrinkItemsController(DrinkItemsService drinkItemsService) {
        this.drinkItemsService = drinkItemsService;
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<DrinkItemsDTOActive> getAllActiveDrinkItems() {
        List<DrinkItemsDTOActive> itemsDTOList = new ArrayList<>();
        for (DrinkItems item: this.drinkItemsService.getAllActive()) {
            itemsDTOList.add(new DrinkItemsDTOActive(item));
        }
        return itemsDTOList;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DrinkItemsDTO getOne(@PathVariable long id) {
        return new DrinkItemsDTO(this.drinkItemsService.getOne(id));
    }

}
