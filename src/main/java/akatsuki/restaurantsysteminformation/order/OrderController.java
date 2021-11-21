package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderBasicInfoDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public OrderBasicInfoDTO getOneWithAll(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new OrderBasicInfoDTO(orderService.getOneWithAll(id));
    }

    @GetMapping("/table/{id}")
    public OrderBasicInfoDTO getOneByRestaurantTable(@PathVariable long id) {
        return new OrderBasicInfoDTO(orderService.getOneByRestaurantTableId(id));
    }

    @GetMapping
    public List<OrderBasicInfoDTO> getAll() {
        return orderService.getAllWithAll().stream().map(OrderBasicInfoDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/active")
    public List<OrderBasicInfoDTO> getAllActive() {
        return orderService.getAllActive().stream().map(OrderBasicInfoDTO::new).collect(Collectors.toList());
    }
}
