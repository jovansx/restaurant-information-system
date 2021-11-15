package akatsuki.restaurantsysteminformation.order.dto;

import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.orderitem.dto.OrderItemIdDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.WaiterBasicInfoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class OrderBasicInfoDTO {

    private Long id;
    private double totalPrice;
    private LocalDateTime createdAt;
    private boolean isDiscarded;
    private boolean isActive;
    private WaiterBasicInfoDTO waiter;
    private List<OrderItemIdDTO> dishItemList;
    private List<OrderItemIdDTO> drinkItemsList;

    public OrderBasicInfoDTO(Order order) {
        this.id = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.createdAt = order.getCreatedAt();
        this.isDiscarded = order.isDiscarded();
        this.isActive = order.isActive();
        this.waiter = new WaiterBasicInfoDTO(order.getWaiter());
        this.dishItemList = new ArrayList<>();
        this.drinkItemsList = new ArrayList<>();
        this.dishItemList = order.getDishes().stream().map(OrderItemIdDTO::new).collect(Collectors.toList());
        this.drinkItemsList = order.getDrinks().stream().map(OrderItemIdDTO::new).collect(Collectors.toList());
    }
}
