package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DrinkItemsUpdateDTO {
    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private int orderId;

    @NotNull
    private List<DrinkItemUpdateDTO> drinkItems;
    private String notes;
}
