package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderBasicInfoDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderBasicInfoDTO> getAll() {
        List<Order> orderList = orderService.getAll();
        List<OrderBasicInfoDTO> orderBasicInfoDTOList = new ArrayList<>();
        for (Order order : orderList) {
            orderBasicInfoDTOList.add(new OrderBasicInfoDTO(order));
        }
        return orderBasicInfoDTOList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody OrderCreateDTO orderCreateDTO) {
        orderService.create(orderCreateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void discard(@PathVariable long id) {
        orderService.discard(id);
    }

    @PutMapping("/charge/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void charge(@PathVariable long id) {
        orderService.charge(id);
    }
}
