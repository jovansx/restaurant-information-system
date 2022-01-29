package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderDTO;
import akatsuki.restaurantsysteminformation.order.exception.*;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private RestaurantTableService restaurantTableService;
    private UnregisteredUserService unregisteredUserService;
    private DrinkItemsService drinkItemsService;
    private ItemService itemService;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository, UnregisteredUserService unregisteredUserService,
                                   RestaurantTableService restaurantTableService, ItemService itemService,
                                   DrinkItemsService drinkItemsService) {
        this.unregisteredUserService = unregisteredUserService;
        this.orderRepository = orderRepository;
        this.restaurantTableService = restaurantTableService;
        this.drinkItemsService = drinkItemsService;
        this.itemService = itemService;
    }

    @Override
    public Order getOneByOrderItem(OrderItem orderItem) {
        List<Order> orderList = getAllWithAll();

        for (Order order : orderList) {
            if (orderItem instanceof DrinkItems) {
                if (order.getDrinks().contains((DrinkItems) orderItem))
                    return order;
            } else {
                if (order.getDishes().contains((DishItem) orderItem))
                    return order;
            }

        }
        throw new OrderNotFoundException("Order that contains dish item with the id of  " + orderItem.getId() + " does not exist in the database.");
    }

    @Override
    public Order getOneWithAll(Long orderId) {
        Order order1 = getOneWithDrinks(orderId);
        Order order2 = getOneWithDishes(orderId);
        order1.setDishes(order2.getDishes());
        List<DrinkItems> items = new ArrayList<>();
        for (DrinkItems di : order1.getDrinks()) {
            items.add(drinkItemsService.findOneWithItems(di.getId()));
        }
        order1.setDrinks(items);
        return order1;
    }

    @Override
    public List<Order> getAllWithAll() {
        List<Long> indexes = orderRepository.findAllIndexes();
        try {
            return indexes.stream().map(this::getOneWithAll).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indexes.stream().map(this::getOneWithAll).collect(Collectors.toList());
    }

    @Override
    public Order create(OrderCreateDTO orderDTO) {
        UnregisteredUser waiter = unregisteredUserService.getOne(orderDTO.getWaiterId());
        if (waiter.getType() != UserType.WAITER)
            throw new UserTypeNotValidException("User has to be waiter!");

        Order order = new Order(0, LocalDateTime.now(), false, true, waiter, new ArrayList<>(), new ArrayList<>());
        orderRepository.save(order);

        restaurantTableService.setOrderToTable(orderDTO.getTableId(), order);

        return order;
    }

    @Override
    public Order delete(long id) {
        Order order = getOneWithAll(id);
        if (order.getDishes().isEmpty() && order.getDrinks().isEmpty())
            orderRepository.deleteById(id);
        else
            throw new OrderDeletionException("Order " + id + " contains order items, it can't be deleted!");
        return order;
    }

    @Override
    public Order updateTotalPriceAndSave(Order order) {
        double totalPrice = 0;
        for (DishItem dishItem : order.getDishes()) {
            double currentPrice = itemService.getCurrentPriceOfItem(dishItem.getItem().getId());
            totalPrice += dishItem.getAmount() * currentPrice;
        }
        for (DrinkItems drinkItems : order.getDrinks()) {
            DrinkItems drinkItems1 = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(drinkItems.getId());
            for (DrinkItem drinkItem : drinkItems1.getDrinkItemList()) {
                double currentPrice = itemService.getCurrentPriceOfItem(drinkItem.getItem().getId());
                totalPrice += drinkItem.getAmount() * currentPrice;
            }
        }
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order charge(long id) {
        Order order = getOneWithAll(id);
        if (order.isDiscarded())
            throw new OrderDiscardException("Order with the id " + id + " is discarded, can't be charged.");
        if (!order.isActive())
            throw new OrderDiscardNotActiveException("Order with the id " + id + " is not active, can't be charged.");
        order.setActive(false);
        order.getDishes().forEach(dish -> dish.setActive(false));
        order.getDrinks().forEach(drinks -> drinks.setActive(false));
        orderRepository.save(order);

        restaurantTableService.changeStateOfTableWithOrder(order, TableState.FREE);

        return order;
    }

    @Override
    public Order discard(long id) {
        Order order = getOneWithAll(id);
        if (order.isDiscarded())
            throw new OrderDiscardException("Order with the id " + id + " is already discarded.");
        if (!order.isActive())
            throw new OrderDiscardNotActiveException("Order with the id " + id + " is not active, can't be discarded.");
        order.setDiscarded(true);
        order.setActive(false);
        order.getDishes().forEach(dish -> dish.setActive(false));
        order.getDrinks().forEach(drinks -> drinks.setActive(false));
        orderRepository.save(order);

        restaurantTableService.changeStateOfTableWithOrder(order, TableState.FREE);

        return order;
    }

    @Override
    public boolean isWaiterActive(UnregisteredUser user) {
        return orderRepository.findAllByActiveIsTrueAndWaiter(user).isEmpty();
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public OrderDTO getOrderByRestaurantTableIdIfWaiterValid(Long tableId, String pinCode) {
        RestaurantTable table = restaurantTableService.getOneWithOrder(tableId);
        OrderDTO orderDTO = new OrderDTO();

        if (table.getActiveOrder() != null) {
            Order order = getOneWithAll(table.getActiveOrder().getId());
            UnregisteredUser waiter = order.getWaiter();
            if (!waiter.getPinCode().equals(pinCode)) {
                throw new OrderWaiterNotValidException("Order with the id " + order.getId() + " does not belong to the waiter " + waiter.getFirstName() + " " + waiter.getLastName());
            }
            orderDTO = new OrderDTO(order);
            restaurantTableService.changeStateOfTableWithOrder(order, TableState.TAKEN);
        }

        unregisteredUserService.checkPinCode(pinCode, UserType.WAITER);

        orderDTO.getDishItemList().sort((d1, d2) -> d1.getState().ordinal() < d2.getState().ordinal() ? -1 : 0);
        orderDTO.getDrinkItemsList().sort((d1, d2) -> d1.getState().ordinal() < d2.getState().ordinal() ? -1 : 0);

        return orderDTO;
    }

    private Order getOneWithDishes(Long orderId) {
        return orderRepository.findByIdAndFetchWaiterAndFetchDishesAndItems(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with the id " + orderId + " is not found in the database."));
    }

    private Order getOneWithDrinks(Long orderId) {
        return orderRepository.findByIdAndFetchWaiterAndDrinks(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order with the id " + orderId + " is not found in the database."));
    }
}
