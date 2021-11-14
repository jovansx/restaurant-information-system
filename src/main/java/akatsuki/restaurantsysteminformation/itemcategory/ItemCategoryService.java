package akatsuki.restaurantsysteminformation.itemcategory;

import java.util.List;

public interface ItemCategoryService {

    ItemCategory findByName(String name);

    void save(ItemCategory itemCategory);

    ItemCategory getOne(long id);

    List<ItemCategory> getAll();

    void create(ItemCategory category);

    void update(ItemCategory category, long id);

    void delete(long id);
}
