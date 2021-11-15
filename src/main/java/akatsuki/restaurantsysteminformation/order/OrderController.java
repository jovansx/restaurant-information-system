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
    public OrderBasicInfoDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new OrderBasicInfoDTO(orderService.getOne(id));
    }

    @GetMapping("/table/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderBasicInfoDTO getOneByRestaurantTable(@PathVariable long id) {
        return new OrderBasicInfoDTO(orderService.getOneByRestaurantTable(id));
    }

    @GetMapping
    public List<OrderBasicInfoDTO> getAll() {
        return orderService.getAll().stream().map(OrderBasicInfoDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderBasicInfoDTO> getAllActive() {
        return orderService.getAllActive().stream().map(OrderBasicInfoDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid OrderCreateDTO orderCreateDTO) {
        orderService.create(orderCreateDTO);
    }

    @PutMapping("/{id}")
    public void discard(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        orderService.discard(id);
    }

    @PutMapping("/charge/{id}")
    public void charge(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        orderService.charge(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        orderService.delete(id);
    }
}
