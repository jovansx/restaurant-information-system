package akatsuki.restaurantsysteminformation.orderitem.dto;

import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class OrderItemIdDTO {

    private @Getter
    @Setter
    Long id;

    public OrderItemIdDTO(OrderItem item) {
        this.id = item.getId();
    }
}
