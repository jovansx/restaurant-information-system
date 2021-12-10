package akatsuki.restaurantsysteminformation.itemcategory.dto;

import akatsuki.restaurantsysteminformation.enums.CategoryType;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryDTO {
    @Getter
    @Setter
    @NotNull(message = "Name cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private String name;

    @Getter
    @Setter
    @NotNull(message = "Category type cannot be null.")
    private CategoryType type;

    public ItemCategoryDTO(ItemCategory itemCategory) {
        this.name = itemCategory.getName();
        this.type = itemCategory.getType();
    }
}
