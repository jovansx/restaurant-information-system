package akatsuki.restaurantsysteminformation.order;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getOne(long id);

}
