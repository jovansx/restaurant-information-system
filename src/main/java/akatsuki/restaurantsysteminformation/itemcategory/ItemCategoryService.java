package akatsuki.restaurantsysteminformation.itemcategory;

import org.springframework.stereotype.Service;

@Service
public class ItemCategoryService {
    private ItemCategoryRepository itemCategoryRepository;

    public void setItemCategoryRepository(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }
}
