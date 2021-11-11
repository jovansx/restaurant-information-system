package akatsuki.restaurantsysteminformation.orderitem.dto;

import akatsuki.restaurantsysteminformation.orderitem.OrderItem;

public class OrderItemIdDTO {

    private Long id;

    public OrderItemIdDTO() {
    }

    public OrderItemIdDTO(OrderItem item) {
        this.id = item.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
