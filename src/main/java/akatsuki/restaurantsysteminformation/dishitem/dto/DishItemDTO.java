package akatsuki.restaurantsysteminformation.dishitem.dto;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemDTO;
import akatsuki.restaurantsysteminformation.enums.ItemState;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DishItemDTO {
    private long id;
    private String notes;
    private String createdAt;
    private ItemState state;
    private String chef;
    private String icon;
    private List<DrinkItemDTOItemOfDishItem> itemList;

    public DishItemDTO(DishItem dishItem) {
        this.id = dishItem.getId();
        if (dishItem.getChef() != null)
            this.chef = dishItem.getChef().getFirstName() + " " + dishItem.getChef().getLastName();
        else
            this.chef = "";
        this.notes = dishItem.getNotes();
        this.createdAt = Timestamp.valueOf(dishItem.getCreatedAt()).getTime() + "";
        this.state = dishItem.getState();
        this.icon = new String(dishItem.getItem().getIconBase64());
        this.itemList = new ArrayList<>();
        itemList.add(new DrinkItemDTOItemOfDishItem(dishItem.getItem(), dishItem.getAmount()));
    }

    public List<DrinkItemDTOItemOfDishItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<DrinkItemDTOItemOfDishItem> itemList) {
        this.itemList = itemList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
