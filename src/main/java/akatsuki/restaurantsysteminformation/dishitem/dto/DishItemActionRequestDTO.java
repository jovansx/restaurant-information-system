package akatsuki.restaurantsysteminformation.dishitem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishItemActionRequestDTO {
    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private long userId;

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private long itemId;
}
