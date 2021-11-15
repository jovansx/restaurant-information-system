package akatsuki.restaurantsysteminformation.dishitem.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class DishItemActionRequestDTO {
    //TODO nisam sig dal nam treba i @NotNUll
    @Min(value = 1, message = "Id has to be a positive value.")
    private long userId;

    @Min(value = 1, message = "Id has to be a positive value.")
    private long itemId;
}
