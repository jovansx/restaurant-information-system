package akatsuki.restaurantsysteminformation.order;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getOne(long id);

    void create(Order order);

    void discard(long id);

    void charge(long id);

}
