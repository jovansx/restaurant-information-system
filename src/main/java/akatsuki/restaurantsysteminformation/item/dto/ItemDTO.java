package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public abstract class ItemDTO {

    @NotEmpty(message = "It cannot be null or empty.")
    private String name;

    private String description;
    private String iconBase64;

    @NotNull(message = "It cannot be null.")
    private ItemType type;

    @NotNull(message = "It cannot be null.")
    private List<String> components;

    public ItemDTO(String name, String description, byte[] icon, ItemType type, List<String> components) {
        this.name = name;
        this.description = description;
        if (icon == null) {
            iconBase64 = "";
        } else {
            iconBase64 = new String(icon);
        }
        this.type = type;
        this.components = components;
    }
}
