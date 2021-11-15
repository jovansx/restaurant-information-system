package akatsuki.restaurantsysteminformation.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemCreateDTO extends ItemDTO {

    @NotNull(message = "It cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private String itemCategory;

    @Positive(message = "Price has to be a positive value.")
    private int price;
}
