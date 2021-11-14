package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.order.exception.OrderDeletionException;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardException;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardNotActiveException;
import akatsuki.restaurantsysteminformation.order.exception.OrderNotFoundException;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UnregisteredUserService unregisteredUserService) {
        this.unregisteredUserService = unregisteredUserService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrderByOrderItem(OrderItem orderItem) {
        List<Order> allOrders = getAll();

        for (Order order : allOrders) {
            if (orderItem instanceof DrinkItems) {
                if (order.getDrinks().contains((DrinkItems) orderItem)) {
                    return order;
                }

            } else {
                if (order.getDishes().contains((DishItem) orderItem)) {
                    return order;
                }
            }

        }
        throw new OrderNotFoundException("Order that contains dish item with the id of  " + orderItem.getId() + " does not exist in the database.");
    }

    @Override
    public Order getOneWithDishes(Long orderId) {
        return orderRepository.findOrderByIdAndFetchDishes(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with the id " + orderId + " is not found in the database."));
    }

    @Override
    public Order getOneWithDrinks(Long orderId) {
        return orderRepository.findOrderByIdAndFetchDrinks(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with the id " + orderId + " is not found in the database."));
    }

    @Override
    public Order getOne(long id) {
        return orderRepository.findOrderByIdFetchWaiter(id).orElseThrow(
                () -> new OrderNotFoundException("Order with the id " + id + " is not found in the database."));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAllFetchWaiter().orElseThrow(
                () -> new OrderNotFoundException("There's no order created."));
    }

    @Override
    public void create(OrderCreateDTO orderDTO) {
        UnregisteredUser waiter = unregisteredUserService.getOne(orderDTO.getWaiterId());   //TODO pomocu anotacija validiraj da nije null ili negativan broj
        if (waiter.getType() != UserType.WAITER) {
            throw new UserTypeNotValidException("User has to be waiter!");
        }
        LocalDateTime createdAt = LocalDateTime.parse(orderDTO.getCreatedAt());     // TODO Datum validiraj pomocu anotacija, za proslost i format
        // TODO Kad saljes sa fronta - format ('2021-11-11T17:35:22')

        Order order = new Order(0, createdAt, false, true, waiter, new HashSet<>(), new HashSet<>());
        orderRepository.save(order);
    }

    @Override
    public void updateTotalPriceAndSave(Order order) {
        orderRepository.save(order);
        double totalPrice = 0;
        for (DishItem dishItem : getOneWithDishes(order.getId()).getDishes()) {
            totalPrice += dishItem.getAmount() * dishItem.getItem().getPrices().get(dishItem.getItem().getPrices().size() - 1).getValue();
        }
        for (DrinkItems drinkItems : getOneWithDrinks(order.getId()).getDrinks()) {
            for (DrinkItem drinkItem : drinkItems.getDrinkItemList()) {
                totalPrice += drinkItem.getAmount() * drinkItem.getItem().getPrices().get(drinkItem.getItem().getPrices().size() - 1).getValue();
            }
        }
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    @Override
    public void discard(long id) {
        Order order = getOne(id);
        if (order.isDiscarded()) {
            throw new OrderDiscardException("Order with the id " + id + " is already discarded.");
        }
        if (!order.isActive()) {
            throw new OrderDiscardNotActiveException("Order with the id " + id + " is not active, can't be discarded.");
        }
        order.setDiscarded(true);
        order.setActive(false);
        order.getDishes().forEach(dish -> dish.setActive(false));
        order.getDrinks().forEach(drinks -> drinks.setActive(false));
        orderRepository.save(order);
    }

    @Override
    public void delete(long id) {
        Order order = getOne(id);
        if (order.getDishes().isEmpty() && order.getDrinks().isEmpty())
            orderRepository.deleteById(id);
        else
            throw new OrderDeletionException("Order " + id + " contains order items, it can't be deleted!");
    }

    @Override
    public void charge(long id) {
        Order order = getOne(id);
        if (order.isDiscarded()) {
            throw new OrderDiscardException("Order with the id " + id + " is discarded, can't be charged.");
        }
        if (!order.isActive()) {
            throw new OrderDiscardNotActiveException("Order with the id " + id + " is not active, can't be charged.");
        }
        order.setActive(false);
        order.getDishes().forEach(dish -> dish.setActive(false));
        order.getDrinks().forEach(drinks -> drinks.setActive(false));
        orderRepository.save(order);
    }


}
