package akatsuki.restaurantsysteminformation.itemcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemCategoryServiceImpl {
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    public void setItemCategoryRepository(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }
}
