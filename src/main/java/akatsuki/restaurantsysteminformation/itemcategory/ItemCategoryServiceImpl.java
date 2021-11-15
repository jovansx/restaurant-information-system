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
    public ItemCategory findByName(String name) {
        return itemCategoryRepository.findByName(name);
    }

    @Override
    public void save(ItemCategory itemCategory) {
        itemCategoryRepository.save(itemCategory);
    }

    @Override
    public ItemCategory getOne(long id) {
        return itemCategoryRepository.findById(id).orElseThrow(
                () -> new ItemCategoryNotFoundException("Item category with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<ItemCategory> getAll() {
        return itemCategoryRepository.findAll();
    }

    @Override
    public void create(ItemCategory category) {
        List<ItemCategory> categories = getAll();
        String formattedName = firstLetterUppercase(category.getName());
        categories.forEach(existingCategory -> {
            if (existingCategory.getName().equals(formattedName)) {
                throw new ItemCategoryNameException("Similar category is recorded with required name " + formattedName + ".");
            }
        });
        category.setName(formattedName);
        itemCategoryRepository.save(category);
    }

    @Override
    public void update(ItemCategory category, long id) {
        ItemCategory foundCategory = getOne(id);
        String formattedName = firstLetterUppercase(category.getName());
        if (foundCategory.getName().equals(formattedName)) {
            throw new ItemCategoryNameException("The Name of chosen category is already set to " + formattedName + ".");
        }
        if (itemCategoryRepository.findByName(formattedName) != null) {
            throw new ItemCategoryNameException("Cannot update to name " + formattedName + ".It already exists in the database.");
        }
        foundCategory.setName(formattedName);
        itemCategoryRepository.save(foundCategory);
    }

    @Override
    public void delete(long id) {
        getOne(id);
        List<Item> items = itemService.getAll();
        items.forEach(item -> {
            if (item.getItemCategory().getId().equals(id)) {
                throw new ItemCategoryDeleteException("Item category with the id " + id + " is contained by other items. It cannot be deleted.");
            }
        });
        itemCategoryRepository.deleteById(id);
    }

    public String firstLetterUppercase(String name) {
        name = name.trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
