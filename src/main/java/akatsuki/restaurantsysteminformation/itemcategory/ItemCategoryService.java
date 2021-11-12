package akatsuki.restaurantsysteminformation.itemcategory;

import java.util.Optional;

public interface ItemCategoryService {

    ItemCategory findByName(String name);

    void save(ItemCategory itemCategory);
}
