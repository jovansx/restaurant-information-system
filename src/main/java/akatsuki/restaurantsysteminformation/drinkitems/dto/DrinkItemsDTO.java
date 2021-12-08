package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemDTO;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DrinkItemsDTO {

    private long id;
    private String notes;
    private String createdAt;
    private ItemState state;
    private List<DrinkItemDTO> itemList;
    private String bartender;

    public DrinkItemsDTO(DrinkItems item) {
        this.id = item.getId();
        if (item.getBartender() != null)
            this.bartender = item.getBartender().getFirstName() + " " + item.getBartender().getLastName();
        else
            this.bartender = "";
        this.notes = item.getNotes();
        this.createdAt = Timestamp.valueOf(item.getCreatedAt()).getTime() + "";
        this.state = item.getState();
        this.itemList = new ArrayList<>();
        for (DrinkItem i : item.getDrinkItemList()) {
            itemList.add(new DrinkItemDTO(i));
        }
    }

}
