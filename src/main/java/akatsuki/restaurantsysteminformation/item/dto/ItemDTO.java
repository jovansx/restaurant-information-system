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
@AllArgsConstructor
public abstract class ItemDTO {

    //TODO vidi ako se bude generisao kod, da validiramo pomocu patterna
    @NotEmpty(message = "It cannot be null or empty.")
    private String code;

    @NotEmpty(message = "It cannot be null or empty.")
    private String name;

    private String description;
    private String iconBase64;

    @NotNull(message = "It cannot be null.")
    private ItemType type;

    @NotNull(message = "It cannot be null.")
    private List<String> components;
}
