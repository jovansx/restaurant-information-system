package akatsuki.restaurantsysteminformation.dishitem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
public class DishItemActionRequestDTO {

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be positive value.")
    private long userId;

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be positive value.")
    private long itemId;
}
