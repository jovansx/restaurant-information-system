package akatsuki.restaurantsysteminformation.drinkitems.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemCreateDTO;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DrinkItemsCreateDTO {

    @NotNull(message = "It cannot be null.")
    @Positive(message = "Id has to be a positive value.")
    private int orderId;

    @NotNull
    private List<DrinkItemCreateDTO> drinkItemList;
    private String notes;
}
