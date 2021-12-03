package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsActiveDTO {
    private Long id;
    private String initials;
    private String name;
    private ItemState state;

    public ItemsActiveDTO(DrinkItems drinkItems) {
        this.id = drinkItems.getId();
        this.state = drinkItems.getState();
        if (drinkItems.getBartender() != null)
            this.initials = drinkItems.getBartender().getFirstName().toUpperCase().charAt(0) + " " + drinkItems.getBartender().getLastName().toUpperCase().charAt(0);
        else
            this.initials = "";
        StringBuilder str = new StringBuilder();
        for (DrinkItem di : drinkItems.getDrinkItemList()) {
            str.append(di.getItem().getName());
            str.append(",");
        }
        this.name = str.substring(0, str.length() - 1);
    }

    public ItemsActiveDTO(DishItem dishItem) {
        this.id = dishItem.getId();
        this.state = dishItem.getState();
        if (dishItem.getChef() != null)
            this.initials = dishItem.getChef().getFirstName().toUpperCase().charAt(0) + " " + dishItem.getChef().getLastName().toUpperCase().charAt(0);
        else
            this.initials = "";
        this.name = dishItem.getItem().getName();
    }
}
