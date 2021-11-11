package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
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

    @Override
    public void create(Order order) {
        if (order.getWaiter().getType() != UserType.WAITER) {
            throw new UserTypeNotValidException("User has to be waiter!");
        }
        double dishesPrice = calculateDishesPrice(order.getDishes());
        double drinksPrice = calculateDrinksPrice(order.getDrinks());
        order.setTotalPrice(dishesPrice + drinksPrice);
        orderRepository.save(order);
    }

    private double calculateDishesPrice(List<DishItem> dishes) {
        double dishesPrice = 0;
        for(DishItem dishItem: dishes) {
            if (dishItem.getItem().getType() != ItemType.DISH) {
                throw new UserTypeNotValidException("Not correct type of dish item!");
            }
            dishesPrice += dishItem.getAmount() * dishItem.getItem().getLastDefinedPrice().getValue();
        }
        return dishesPrice;
    }

    private double calculateDrinksPrice(List<DrinkItems> drinkItemsList) {
        double drinksPrice = 0;
        for (DrinkItems drinkItems: drinkItemsList) {
            List<DrinkItem> drinkItemList = drinkItems.getDrinkItems();
            for(DrinkItem drinkItem: drinkItemList) {
                if (drinkItem.getItem().getType() != ItemType.DRINK) {
                    throw new UserTypeNotValidException("Not correct type of drink item!");
                }
                drinksPrice += drinkItem.getAmount() * drinkItem.getItem().getLastDefinedPrice().getValue();
            }
        }
        return drinksPrice;
    }
}
