package akatsuki.restaurantsysteminformation.drinkitems.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class DrinkItemsActionRequestDTO {

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private long userId;

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private long itemId;
}
