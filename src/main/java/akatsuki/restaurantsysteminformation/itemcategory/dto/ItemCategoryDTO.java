package akatsuki.restaurantsysteminformation.itemcategory.dto;

import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ItemCategoryDTO {
    @NotNull(message = "Name cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    String name;

    public ItemCategoryDTO() {
    }

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
