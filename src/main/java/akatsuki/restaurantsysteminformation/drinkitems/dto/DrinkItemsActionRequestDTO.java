package akatsuki.restaurantsysteminformation.drinkitems.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class DrinkItemsActionRequestDTO {

    @Min(value = 1, message = "Id has to be a positive value.")
    private long userId;

    @Min(value = 1, message = "Id has to be a positive value.")
    private long itemId;
}
