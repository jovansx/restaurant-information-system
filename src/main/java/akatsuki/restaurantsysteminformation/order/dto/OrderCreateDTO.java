package akatsuki.restaurantsysteminformation.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

    @NotNull(message = "It has to be null.")
    @Positive(message = "It has to be a positive value.")
    private @Getter
    @Setter
    Long waiterId;
}
