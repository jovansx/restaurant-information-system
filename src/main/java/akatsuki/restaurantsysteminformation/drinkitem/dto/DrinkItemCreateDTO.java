package akatsuki.restaurantsysteminformation.drinkitem.dto;

public class DrinkItemCreateDTO {
    private int amount;
    private Long itemId;

    public DrinkItemCreateDTO() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
