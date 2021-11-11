package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOne(long id) {
        return orderRepository.findOrderByIdFetchWaiter(id).orElseThrow(
                () -> new UserNotFoundException("Order with the id " + id + " is not found in the database."));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAllFetchWaiter().orElseThrow(
                () -> new UserNotFoundException("There's no order created."));
    }
}
