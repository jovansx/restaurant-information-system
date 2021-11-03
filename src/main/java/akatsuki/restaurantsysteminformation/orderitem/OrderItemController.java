package akatsuki.restaurantsysteminformation.orderitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order-item")
public class OrderItemController {
    private OrderItemServiceImpl orderItemService;

    @Autowired
    public OrderItemController(OrderItemServiceImpl orderItemService) {
        this.orderItemService = orderItemService;
    }
}
