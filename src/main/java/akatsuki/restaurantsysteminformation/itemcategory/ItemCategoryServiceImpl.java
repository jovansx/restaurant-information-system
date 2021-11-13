package akatsuki.restaurantsysteminformation.itemcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    public void setItemCategoryRepository(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }

    @Override
    public ItemCategory findByName(String name) {
        return itemCategoryRepository.findByName(name);
    }

    @Override
    public void save(ItemCategory itemCategory) {
        itemCategoryRepository.save(itemCategory);
    }
}
