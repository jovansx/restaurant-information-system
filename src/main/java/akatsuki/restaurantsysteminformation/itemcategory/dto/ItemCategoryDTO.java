package akatsuki.restaurantsysteminformation.itemcategory.dto;

import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
public class ItemCategoryDTO {
    @Getter
    @Setter
    @NotNull(message = "Name cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private String name;

    public ItemCategoryDTO(ItemCategory itemCategory) {
        this.name = itemCategory.getName();
    }
}
