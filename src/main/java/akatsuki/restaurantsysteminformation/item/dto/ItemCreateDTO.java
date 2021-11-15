package akatsuki.restaurantsysteminformation.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemCreateDTO extends ItemDTO {

    @NotEmpty(message = "It cannot be null or empty.")
    private String itemCategory;

    @Positive(message = "Price has to be a positive value.")
    private int price;
}
