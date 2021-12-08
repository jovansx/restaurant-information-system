package akatsuki.restaurantsysteminformation.order.dto;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemOrderRepresentationDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsOrderRepresentationDTO;
import akatsuki.restaurantsysteminformation.order.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    private long id;
    private double totalPrice;
    private String createdAt;
    private String waiter;
    private long waiterId;
    private List<DishItemOrderRepresentationDTO> dishItemList;
    private List<DrinkItemsOrderRepresentationDTO> drinkItemsList;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.createdAt = order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.waiter = order.getWaiter().getFirstName() + " " + order.getWaiter().getLastName();
        this.waiterId = order.getWaiter().getId();
        this.dishItemList = new ArrayList<>();
        this.drinkItemsList = new ArrayList<>();
        this.dishItemList = order.getDishes().stream().map((dish) -> new DishItemOrderRepresentationDTO(dish)).collect(Collectors.toList());
        this.drinkItemsList = order.getDrinks().stream().map((drinks) -> new DrinkItemsOrderRepresentationDTO(drinks)).collect(Collectors.toList());
    }

    public OrderDTO() {
    }

    public long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(long waiterId) {
        this.waiterId = waiterId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public List<DishItemOrderRepresentationDTO> getDishItemList() {
        return dishItemList;
    }

    public void setDishItemList(List<DishItemOrderRepresentationDTO> dishItemList) {
        this.dishItemList = dishItemList;
    }

    public List<DrinkItemsOrderRepresentationDTO> getDrinkItemsList() {
        return drinkItemsList;
    }

    public void setDrinkItemsList(List<DrinkItemsOrderRepresentationDTO> drinkItemsList) {
        this.drinkItemsList = drinkItemsList;
    }
}
