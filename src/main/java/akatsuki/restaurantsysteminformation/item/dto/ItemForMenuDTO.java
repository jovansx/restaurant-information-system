package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemForMenuDTO {
    private Long id;
    private String name;
    private String iconBase64;

    public ItemForMenuDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.iconBase64 = new String(item.getIconBase64());
    }
}
