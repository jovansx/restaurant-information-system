package akatsuki.restaurantsysteminformation.itemcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item-category")
public class ItemCategoryController {
    private ItemCategoryServiceImpl itemCategoryService;

    @Autowired
    public ItemCategoryController(ItemCategoryServiceImpl itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
    }
}
