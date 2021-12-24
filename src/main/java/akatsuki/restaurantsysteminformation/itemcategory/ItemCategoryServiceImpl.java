package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryDeleteException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNameException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCategoryServiceImpl implements ItemCategoryService {
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemService itemService;

    @Override
    public ItemCategory getOne(long id) {
        return itemCategoryRepository.findById(id).orElseThrow(
                () -> new ItemCategoryNotFoundException("Item category with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public ItemCategory getByName(String name) {
        return itemCategoryRepository.findByName(name);
    }

    @Override
    public List<ItemCategory> getAll() {
        return itemCategoryRepository.findAll();
    }

    @Override
    public ItemCategory create(ItemCategory category) {
        List<ItemCategory> categories = getAll();
        String formattedName = firstLetterUppercase(category.getName());
        categories.forEach(existingCategory -> {
            if (existingCategory.getName().equals(formattedName))
                throw new ItemCategoryNameException("Similar category is recorded with required name " + formattedName + ".");
        });
        category.setName(formattedName);
        category.setType(category.getType());
        itemCategoryRepository.save(category);
        return category;
    }

    @Override
    public ItemCategory delete(long id) {
        ItemCategory category = getOne(id);
        List<Item> items = itemService.getAllWithAll();
        items.forEach(item -> {
            if (item.getItemCategory().getId().equals(id))
                throw new ItemCategoryDeleteException("Item category with the id " + id + " is contained by other items. It cannot be deleted.");
        });
        itemCategoryRepository.deleteById(id);
        return category;
    }

    @Override
    public String firstLetterUppercase(String name) {
        name = name.trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
