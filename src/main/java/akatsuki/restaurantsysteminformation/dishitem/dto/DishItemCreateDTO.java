package akatsuki.restaurantsysteminformation.dishitem.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class DishItemCreateDTO {

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private Long itemId;

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private int amount;

    private String notes;

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private Long orderId;
}
