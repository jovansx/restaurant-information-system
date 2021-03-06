package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ItemUpdateDTO extends ItemDTO {
    private ItemCategoryDTO itemCategory;

    @Positive(message = "Price has to be a positive value.")
    private int price;

    @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", message = "It has to be uuid pattern.")
    private String code;

    public ItemUpdateDTO(ItemCategoryDTO itemCategory, int price, String code, String name, String description, byte[] icon, ItemType type, List<String> components) {
        super(name, description, icon, type, components);
        this.itemCategory = itemCategory;
        this.price = price;
        this.code = code;
    }
}
