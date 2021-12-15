package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrinkItemsCreateDTO {

    @NotNull(message = "It cannot be null.")
    private int orderId;

    @NotNull
    private List<DrinkItemUpdateDTO> drinkItems;
    private String notes;

    private OrderCreateDTO orderCreateDTO;
}
