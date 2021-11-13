package akatsuki.restaurantsysteminformation.drinkitems.dto;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemCreateDTO;

import java.util.List;

public class DrinkItemsCreateDTO {
    private int orderId;
    private List<DrinkItemCreateDTO> drinkItemList;
    private String notes;

    public DrinkItemsCreateDTO() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<DrinkItemCreateDTO> getDrinkItemList() {
        return drinkItemList;
    }

    public void setDrinkItemList(List<DrinkItemCreateDTO> drinkItemList) {
        this.drinkItemList = drinkItemList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
