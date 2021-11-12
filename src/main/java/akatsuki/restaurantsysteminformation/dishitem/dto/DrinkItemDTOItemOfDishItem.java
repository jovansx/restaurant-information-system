package akatsuki.restaurantsysteminformation.dishitem.dto;

import akatsuki.restaurantsysteminformation.item.Item;

public class DrinkItemDTOItemOfDishItem {
    private String itemName;
    private int amount;

    public DrinkItemDTOItemOfDishItem(Item item, int amount) {
        this.itemName = item.getName();
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
