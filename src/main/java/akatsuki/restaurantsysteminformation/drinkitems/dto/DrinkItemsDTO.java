package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemDTO;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.enums.ItemState;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DrinkItemsDTO {

    private long id;
    private String notes;
    private String createdAt;
    private ItemState state;
    private List<DrinkItemDTO> drinkItemList;
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
        this.drinkItemList = new ArrayList<>();
        for (DrinkItem i : item.getDrinkItemList()) {
            drinkItemList.add(new DrinkItemDTO(i));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBartender() {
        return bartender;
    }

    public void setBartender(String bartender) {
        this.bartender = bartender;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ItemState getState() {
        return state;
    }

    public void setState(ItemState state) {
        this.state = state;
    }

    public List<DrinkItemDTO> getDrinkItemList() {
        return drinkItemList;
    }

    public void setDrinkItemList(List<DrinkItemDTO> drinkItemList) {
        this.drinkItemList = drinkItemList;
    }
}
