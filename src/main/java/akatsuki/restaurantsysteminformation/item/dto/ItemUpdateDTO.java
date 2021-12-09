package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ItemUpdateDTO extends ItemDTO {

    @NotNull(message = "It cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private ItemCategoryDTO itemCategory;

    @Positive(message = "Price has to be a positive value.")
    private int price;

    //TODO: add validation
    private String code;

    public ItemUpdateDTO(ItemCategoryDTO itemCategory, int price, String code, String name, String description, byte[] icon, ItemType type, List<String> components) {
        super(name, description, icon, type, components);
        this.itemCategory = itemCategory;
        this.price = price;
        this.code = code;
    }
}
