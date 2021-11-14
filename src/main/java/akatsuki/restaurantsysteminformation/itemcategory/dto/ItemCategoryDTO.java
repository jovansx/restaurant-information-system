package akatsuki.restaurantsysteminformation.itemcategory.dto;

import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;

public class ItemCategoryDTO {
    String name;

    public ItemCategoryDTO() {}

    public ItemCategoryDTO(ItemCategory itemCategory) {
        this.name = itemCategory.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
