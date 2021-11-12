package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService{
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    public void setItemCategoryRepository(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }

    @Override
    public ItemCategory findByName(String name) {
        return itemCategoryRepository.findByName(name).get();
    }

    @Override
    public void save(ItemCategory itemCategory) {
        itemCategoryRepository.save(itemCategory);
    }
}
