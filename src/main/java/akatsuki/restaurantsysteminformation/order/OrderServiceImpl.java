package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.dishitem.DishItemService;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItemService;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardException;
import akatsuki.restaurantsysteminformation.order.exception.OrderDiscardNotActiveException;
import akatsuki.restaurantsysteminformation.order.exception.OrderNotFoundException;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final DishItemService dishItemService;
    private final DrinkItemService drinkItemService;
    private final DrinkItemsService drinkItemsService;
    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ItemService itemService, DishItemService dishItemService,
                            DrinkItemService drinkItemService, DrinkItemsService drinkItemsService, UnregisteredUserService unregisteredUserService) {
        this.itemService = itemService;
        this.dishItemService = dishItemService;
        this.drinkItemService = drinkItemService;
        this.drinkItemsService = drinkItemsService;
        this.unregisteredUserService = unregisteredUserService;
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
    public void create(OrderCreateDTO orderDTO) {

        UnregisteredUser waiter = unregisteredUserService.getOne(orderDTO.getWaiterId());   //TODO pomocu anotacija validiraj da nije null ili negativan broj
        if (waiter.getType() != UserType.WAITER) {
            throw new UserTypeNotValidException("User has to be waiter!");
        }
        List<DishItemCreateDTO> dishItemCreateDTOList = orderDTO.getDishItemList();     //TODO validiraj da nisu null liste
        checkDishesExistence(dishItemCreateDTOList);
        List<DrinkItemsCreateDTO> drinkItemsCreateDTOList = orderDTO.getDrinkItemsList();
        checkListItemsExistence(drinkItemsCreateDTOList);
        LocalDateTime createdAt = LocalDateTime.parse(orderDTO.getCreatedAt());     // TODO Datum validiraj pomocu anotacija, za proslost i format
        // TODO Kad saljes sa fronta - format ('2021-11-11T17:35:22')

        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);
        List<DishItem> dishItemsList = new ArrayList<>();
        dishItemCreateDTOList.forEach(dishItemDTO -> {
            Item item = itemService.getOne(dishItemDTO.getItemId());
            DishItem dishItem = new DishItem(dishItemDTO.getNotes(), createdAt, false, ItemState.NEW, dishItemDTO.getAmount(), null, item, true);
            DishItem savedDishItem = dishItemService.create(dishItem);
            dishItemsList.add(savedDishItem);
            totalPrice.updateAndGet(value -> value + savedDishItem.getAmount() * item.getLastDefinedPrice());
        });

        List<DrinkItems> drinkItemsList = new ArrayList<>();
        drinkItemsCreateDTOList.forEach(drinkItemsDTO -> {
            List<DrinkItemCreateDTO> drinkItemCreateDTOList = drinkItemsDTO.getDrinkItemList();
            List<DrinkItem> drinkItemList = new ArrayList<>();
            drinkItemCreateDTOList.forEach(drinkItemDTO -> {
                Item item = itemService.getOne(drinkItemDTO.getItemId());
                DrinkItem drinkItem = new DrinkItem(drinkItemDTO.getAmount(), item);
                DrinkItem savedDrinkItem = drinkItemService.create(drinkItem);
                drinkItemList.add(savedDrinkItem);
                totalPrice.updateAndGet(value -> value + savedDrinkItem.getAmount() * item.getLastDefinedPrice());
            });
            DrinkItems drinkItems = new DrinkItems(drinkItemsDTO.getNotes(), createdAt, false, ItemState.NEW, null, drinkItemList, true);
            DrinkItems savedDrinkItems = drinkItemsService.create(drinkItems);
            drinkItemsList.add(savedDrinkItems);
        });

        Order order = new Order(totalPrice.get(), createdAt, false, true, waiter, dishItemsList, drinkItemsList);
        orderRepository.save(order);
    }

    private void checkDishesExistence(List<DishItemCreateDTO> collection) {
        collection.forEach(dto -> {
            Item item = itemService.getOne(dto.getItemId());
            if (item.getType() != ItemType.DISH) {
                throw new UserTypeNotValidException("Not correct type of dish item!");
            }
        });
    }

    private void checkListItemsExistence(List<DrinkItemsCreateDTO> collection) {
        collection.forEach(list -> {
            list.getDrinkItemList().forEach(dto -> {
                Item item = itemService.getOne(dto.getItemId());
                if (item.getType() != ItemType.DRINK) {
                    throw new UserTypeNotValidException("Not correct type of drink item!");
                }
            });
        });
    }

    @Override
    public void discard(long id) {
        Order order = checkOrderExistence(id);
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
    public void charge(long id) {
        Order order = checkOrderExistence(id);
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

    private Order checkOrderExistence(long id) {
        Optional<Order> foundOrder = orderRepository.findById(id);
        if (foundOrder.isEmpty()) {
            throw new OrderNotFoundException("Order with the id " + id + " is not found in the database.");
        }
        return foundOrder.get();
    }
}
