package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemBasicInfoDTO extends ItemForMenuDTO {
    private String itemType;

    public ItemBasicInfoDTO(Item item) {
        super(item);
        this.itemType = item.getType().toString().toLowerCase();
    }
}
