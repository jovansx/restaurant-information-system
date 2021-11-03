package akatsuki.restaurantsysteminformation.itemcategory;

import org.springframework.stereotype.Service;

@Service
public class ItemCategoryServiceImpl {
    private ItemCategoryRepository itemCategoryRepository;

    public void setItemCategoryRepository(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }
}
