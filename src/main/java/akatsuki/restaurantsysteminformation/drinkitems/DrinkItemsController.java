package akatsuki.restaurantsysteminformation.drinkitems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("drink-items")
public class DrinkItemsController {
    private final DrinkItemsServiceImpl drinkItemsService;

    @Autowired
    public DrinkItemsController(DrinkItemsServiceImpl drinkItemsService) {
        this.drinkItemsService = drinkItemsService;
    }
}
