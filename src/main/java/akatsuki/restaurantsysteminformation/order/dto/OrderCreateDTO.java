package akatsuki.restaurantsysteminformation.order.dto;

public class OrderCreateDTO {
    private Long waiterId;
    private String createdAt;

    public OrderCreateDTO() {
    }

    public Long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
