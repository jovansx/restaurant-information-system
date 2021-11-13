package akatsuki.restaurantsysteminformation.drinkitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("drink-item")
public class DrinkItemController {
    private final DrinkItemServiceImpl drinkItemService;

    //TODO CRUD
    @Autowired
    public DrinkItemController(DrinkItemServiceImpl drinkItemService) {
        this.drinkItemService = drinkItemService;
    }
}
