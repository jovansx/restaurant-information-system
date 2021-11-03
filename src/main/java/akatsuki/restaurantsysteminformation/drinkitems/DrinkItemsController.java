package akatsuki.restaurantsysteminformation.drinkitems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("drink-items")
public class DrinkItemsController {
    private DrinkItemsService drinkItemsService;

    @Autowired
    public DrinkItemsController(DrinkItemsService drinkItemsService) {
        this.drinkItemsService = drinkItemsService;
    }
}
