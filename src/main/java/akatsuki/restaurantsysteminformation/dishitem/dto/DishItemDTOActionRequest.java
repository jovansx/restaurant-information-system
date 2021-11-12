package akatsuki.restaurantsysteminformation.dishitem.dto;

public class DishItemDTOActionRequest {

    private long userId;
    private long itemId;

    public DishItemDTOActionRequest(long userId, long itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

}
