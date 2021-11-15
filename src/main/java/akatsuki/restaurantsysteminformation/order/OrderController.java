package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderBasicInfoDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderBasicInfoDTO getOne(@PathVariable long id) {
        return new OrderBasicInfoDTO(orderService.getOne(id));
    }

    @GetMapping("/table/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderBasicInfoDTO getOneByRestaurantTable(@PathVariable long id) {
        return new OrderBasicInfoDTO(orderService.getOneByRestaurantTable(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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
    public void create(@RequestBody OrderCreateDTO orderCreateDTO) {
        orderService.create(orderCreateDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void discard(@PathVariable long id) {
        orderService.discard(id);
    }

    @PutMapping("/charge/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void charge(@PathVariable long id) {
        orderService.charge(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        orderService.delete(id);
    }
}
