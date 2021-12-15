package akatsuki.restaurantsysteminformation.itemcategory;

import java.util.List;

public interface ItemCategoryService {
    ItemCategory getOne(long id);

    ItemCategory getByName(String name);

    List<ItemCategory> getAll();

    ItemCategory create(ItemCategory category);

    ItemCategory delete(long id);

    String firstLetterUppercase(String name);
}
