package akatsuki.restaurantsysteminformation.itemcategory;

import java.util.List;

public interface ItemCategoryService {
    ItemCategory getOne(long id);

    ItemCategory getByName(String name);

    List<ItemCategory> getAll();

    ItemCategory create(ItemCategory category);

    ItemCategory update(ItemCategory category, long id);

    ItemCategory delete(long id);

    void save(ItemCategory itemCategory);

    String firstLetterUppercase(String name);
}
