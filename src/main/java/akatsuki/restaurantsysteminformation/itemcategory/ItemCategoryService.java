package akatsuki.restaurantsysteminformation.itemcategory;

import java.util.List;

public interface ItemCategoryService {
    ItemCategory getOne(long id);

    List<ItemCategory> getAll();

    void create(ItemCategory category);

    void update(ItemCategory category, long id);

    void delete(long id);

    ItemCategory findByName(String name);

    void save(ItemCategory itemCategory);

    String firstLetterUppercase(String name);
}
