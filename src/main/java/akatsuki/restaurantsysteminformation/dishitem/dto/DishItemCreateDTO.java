package akatsuki.restaurantsysteminformation.dishitem.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class DishItemCreateDTO {

    @Min(value = 1, message = "It has to be a positive value.")
    private Long itemId;

    @Min(value = 1, message = "It has to be a positive value.")
    private int amount;

    private String notes;

    @Min(value = 1, message = "It has to be a positive value.")
    private Long orderId;
}
