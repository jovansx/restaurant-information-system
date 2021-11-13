package akatsuki.restaurantsysteminformation.dishitem.dto;

public class DishItemCreateDTO {
    //TODO validiraj da je itemId i orderId i amount positivan
    private Long itemId;
    private int amount;
    private String notes;
    private Long orderId;

    public DishItemCreateDTO() {
    }

    public DishItemCreateDTO(Long itemId, int amount, String notes, Long orderId) {
        this.itemId = itemId;
        this.amount = amount;
        this.notes = notes;
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
