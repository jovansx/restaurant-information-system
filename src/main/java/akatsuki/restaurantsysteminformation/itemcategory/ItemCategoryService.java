package akatsuki.restaurantsysteminformation.itemcategory;

public interface ItemCategoryService {

    ItemCategory findByName(String name);

    void save(ItemCategory itemCategory);
}
