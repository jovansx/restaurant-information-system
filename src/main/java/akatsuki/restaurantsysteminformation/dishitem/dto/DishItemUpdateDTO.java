package akatsuki.restaurantsysteminformation.dishitem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class DishItemUpdateDTO {
    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private long id;

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private int amount;

    private String notes;

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private long orderId;
}
