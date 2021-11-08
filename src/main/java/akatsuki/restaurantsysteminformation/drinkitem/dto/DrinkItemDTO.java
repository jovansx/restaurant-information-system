package akatsuki.restaurantsysteminformation.drinkitem.dto;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;

public class DrinkItemDTO {
    private long id;
    private int amount;
    private String itemName;

    public DrinkItemDTO(DrinkItem drinkItem) {
        this.id = drinkItem.getId();
        this.amount = drinkItem.getAmount();
        this.itemName = drinkItem.getItem().getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
