package akatsuki.restaurantsysteminformation.dishitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dish-tem")
public class DishItemController {
    private DishItemService dishItemService;

    @Autowired
    public DishItemController(DishItemService dishItemService) {
        this.dishItemService = dishItemService;
    }

}
