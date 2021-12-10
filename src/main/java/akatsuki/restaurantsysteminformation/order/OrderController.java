package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderBasicInfoDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    //    TODO Ovo se ne koristi valjda
    @GetMapping("/{id}")
    public OrderBasicInfoDTO getOneWithAll(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new OrderBasicInfoDTO(orderService.getOneWithAll(id));
    }

    //    TODO Ovo se ne koristi valjda
    @GetMapping("/table/{id}")
    public OrderBasicInfoDTO getOneByRestaurantTable(@PathVariable long id) {
        return new OrderBasicInfoDTO(orderService.getOneByRestaurantTableId(id));
    }

    @GetMapping("/{tableId}/{pinCode}")
    public OrderDTO getOrderByRestaurantTable(@PathVariable Long tableId, @PathVariable String pinCode) {
        return orderService.getOrderByRestaurantTableIdIfWaiterValid(tableId, pinCode);
    }

    //    TODO Ovo se ne koristi valjda
    @GetMapping
    public List<OrderBasicInfoDTO> getAll() {
        return orderService.getAllWithAll().stream().map(OrderBasicInfoDTO::new).collect(Collectors.toList());
    }

    //    TODO Ovo se ne koristi valjda
    @GetMapping("/active")
    public List<OrderBasicInfoDTO> getAllActive() {
        return orderService.getAllActive().stream().map(OrderBasicInfoDTO::new).collect(Collectors.toList());
    }
}
