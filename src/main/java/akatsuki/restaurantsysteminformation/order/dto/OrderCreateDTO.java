package akatsuki.restaurantsysteminformation.order.dto;

public class OrderCreateDTO {
    private Long waiterId;

    public OrderCreateDTO() {
    }

    public Long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }

}
