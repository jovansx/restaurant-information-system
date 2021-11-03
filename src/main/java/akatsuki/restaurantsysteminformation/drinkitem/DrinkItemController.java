package akatsuki.restaurantsysteminformation.drinkitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("drink-item")
public class DrinkItemController {
    private DrinkItemService drinkItemService;

    @Autowired
    public DrinkItemController(DrinkItemService drinkItemService) {
        this.drinkItemService = drinkItemService;
    }
}
