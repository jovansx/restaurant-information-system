package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.dishitem.DishItemService;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.drinkitem.DrinkItemService;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitem.mapper.DrinkItemMapper;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.mapper.DrinkItemsMapper;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemService;
import akatsuki.restaurantsysteminformation.order.dto.OrderBasicInfoDTO;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.mapper.DishItemMapper;
import akatsuki.restaurantsysteminformation.order.mapper.OrderMapper;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;
    private final ItemService itemService;
    private final DishItemService dishItemService;
    private final DrinkItemService drinkItemService;
    private final DrinkItemsService drinkItemsService;
    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public OrderController(OrderService orderService, ItemService itemService, DishItemService dishItemService,
                           DrinkItemService drinkItemService, DrinkItemsService drinkItemsService, UnregisteredUserService unregisteredUserService) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.dishItemService = dishItemService;
        this.drinkItemService = drinkItemService;
        this.drinkItemsService = drinkItemsService;
        this.unregisteredUserService = unregisteredUserService;
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

        List<DishItemCreateDTO> dishItemCreateDTOList = orderCreateDTO.getDishItemList();
        List<DrinkItemsCreateDTO> drinkItemsCreateDTOList = orderCreateDTO.getDrinkItemsList();
        LocalDateTime createdAt = LocalDateTime.parse(orderCreateDTO.getCreatedAt());
        // '2021-11-11T17:35:22.068Z' kad saljes sa fronta ukini ovo .068Z
        List<DishItem> dishItems = new ArrayList<>();
        dishItemCreateDTOList.forEach(dishItemDTO -> {
            Item item = itemService.getOne(dishItemDTO.getItemId());
            DishItem dishItem = DishItemMapper.convertDishItemCreateDTOToDishItem(dishItemDTO, createdAt, item);
            DishItem savedDishItem = dishItemService.create(dishItem);
            dishItems.add(savedDishItem);
        });

        List<DrinkItems> drinkItemsList = new ArrayList<>();
        drinkItemsCreateDTOList.forEach(drinkItemsDTO -> {
            List<DrinkItemCreateDTO> drinkItemCreateDTOList = drinkItemsDTO.getDrinkItemList();
            List<DrinkItem> drinkItemList = new ArrayList<>();
            drinkItemCreateDTOList.forEach(drinkItemDTO -> {
                Item item = itemService.getOne(drinkItemDTO.getItemId());
                DrinkItem drinkItem = DrinkItemMapper.convertDrinkItemCreateDTOToDrinkItem(drinkItemDTO.getAmount(), item);
                DrinkItem savedDrinkItem = drinkItemService.create(drinkItem);
                drinkItemList.add(savedDrinkItem);
            });
            DrinkItems drinkItems = DrinkItemsMapper.convertDishItemCreateDTOToDishItem(drinkItemsDTO, createdAt, drinkItemList);
            DrinkItems savedDrinkItems = drinkItemsService.create(drinkItems);
            drinkItemsList.add(savedDrinkItems);
        });

        UnregisteredUser waiter = unregisteredUserService.getOne(orderCreateDTO.getWaiterId());
        Order order = OrderMapper.convertOrderCreateDTOToOrder(createdAt, dishItems, drinkItemsList, waiter);
        orderService.create(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void discard(@PathVariable long id) {
        orderService.discard(id);
    }
}
