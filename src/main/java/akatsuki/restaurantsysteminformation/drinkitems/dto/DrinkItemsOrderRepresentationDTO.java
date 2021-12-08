package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemDTO;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrinkItemsOrderRepresentationDTO {
    private long id;
    private String notes;
    private ItemState state;
    private List<DrinkItemDTO> itemList;
    private String name;

    public DrinkItemsOrderRepresentationDTO(DrinkItems drinks) {
        this.id = drinks.getId();
        this.notes = drinks.getNotes();
        this.state = drinks.getState();
        this.itemList = new ArrayList<>();
        this.name = "";
        for (int i = 0; i < drinks.getDrinkItemList().size(); i++) {
            itemList.add(new DrinkItemDTO(drinks.getDrinkItemList().get(i)));
            if (i == drinks.getDrinkItemList().size() - 1) {
                name += drinks.getDrinkItemList().get(i).getItem().getName();
                break;
            }
            name += drinks.getDrinkItemList().get(i).getItem().getName() + ", ";
        }
    }
}
