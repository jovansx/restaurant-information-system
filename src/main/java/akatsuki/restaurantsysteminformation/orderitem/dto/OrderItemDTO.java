package akatsuki.restaurantsysteminformation.orderitem.dto;

import akatsuki.restaurantsysteminformation.enums.ItemState;

import java.time.LocalDateTime;

public class OrderItemDTO {
    private long id;
    private String notes;
    private LocalDateTime createdAt;
    private ItemState state;
}
