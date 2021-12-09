package akatsuki.restaurantsysteminformation.dishitem.dto;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishItemOrderRepresentationDTO {
    private long id;
    private String notes;
    private ItemState state;
    private String icon;
    private DishItemOrderedDTO orderedItem;

    public DishItemOrderRepresentationDTO(DishItem dish) {
        this.id = dish.getId();
        this.notes = dish.getNotes();
        this.state = dish.getState();
        if (dish.getItem().getIconBase64() != null)
            this.icon = new String(dish.getItem().getIconBase64());
        else
            this.icon = "";
        orderedItem = new DishItemOrderedDTO(dish.getItem().getName(), dish.getAmount());
    }
}
