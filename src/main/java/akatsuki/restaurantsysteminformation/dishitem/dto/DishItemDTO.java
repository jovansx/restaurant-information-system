package akatsuki.restaurantsysteminformation.dishitem.dto;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishItemDTO {

    private long id;
    private String notes;
    private String createdAt;
    private ItemState state;
    private String chef;
    private String icon;
    private List<DishItemOrderedDTO> itemList;

    public DishItemDTO(DishItem dishItem) {
        this.id = dishItem.getId();
        if (dishItem.getChef() != null)
            this.chef = dishItem.getChef().getFirstName() + " " + dishItem.getChef().getLastName();
        else
            this.chef = "";
        this.notes = dishItem.getNotes();
        this.createdAt = Timestamp.valueOf(dishItem.getCreatedAt()).getTime() + "";
        this.state = dishItem.getState();
        //TODO: Kad namestimo da za h2 bazu mogu ikone izbrisi ovaj if i ostavi samo prvi deo
        if (dishItem.getItem().getIconBase64() != null)
            this.icon = new String(dishItem.getItem().getIconBase64());
        else
            this.icon = "";
        this.itemList = new ArrayList<>();
        itemList.add(new DishItemOrderedDTO(dishItem.getItem().getName(), dishItem.getAmount()));
    }
}
