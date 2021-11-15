package akatsuki.restaurantsysteminformation.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
public class OrderCreateDTO {

    @NotNull(message = "It has to be null.")
    @Positive(message = "It has to be a positive value.")
    //TODO @Digits validation, vidi i na drugim mestima
    private @Getter @Setter Long waiterId;
}
