package akatsuki.restaurantsysteminformation.order.dto;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.orderitem.dto.OrderItemIdDTO;
import akatsuki.restaurantsysteminformation.user.dto.WaiterBasicInfoDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderBasicInfoDTO {

    private Long id;
    private double totalPrice;
    private LocalDateTime createdAt;
    private boolean isDiscarded;
    private boolean isActive;
    private WaiterBasicInfoDTO waiter;
    private List<OrderItemIdDTO> dishItemList;
    private List<OrderItemIdDTO> drinkItemsList;

    public OrderBasicInfoDTO(Order order) {
        this.id = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.createdAt = order.getCreatedAt();
        this.isDiscarded = order.isDiscarded();
        this.isActive = order.isActive();
        this.waiter = new WaiterBasicInfoDTO(order.getWaiter());
        this.dishItemList = new ArrayList<>();
        this.drinkItemsList = new ArrayList<>();
        setDishItemsInitially(order.getDishes());
        setDrinkItemsInitially(order.getDrinks());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDiscarded() {
        return isDiscarded;
    }

    public void setDiscarded(boolean discarded) {
        isDiscarded = discarded;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public WaiterBasicInfoDTO getWaiter() {
        return waiter;
    }

    public void setWaiter(WaiterBasicInfoDTO waiterDTO) {
        this.waiter = waiterDTO;
    }

    public List<OrderItemIdDTO> getDishItemList() {
        return dishItemList;
    }

    public void setDishItemList(List<OrderItemIdDTO> dishItemList) {
        this.dishItemList = dishItemList;
    }

    public List<OrderItemIdDTO> getDrinkItems() {
        return drinkItemsList;
    }

    public void setDrinkItemsList(List<OrderItemIdDTO> drinkItems) {
        this.drinkItemsList = drinkItems;
    }

    private void setDishItemsInitially(Set<DishItem> items) {
        for (DishItem item : items) {
            this.dishItemList.add(new OrderItemIdDTO(item));
        }
    }

    private void setDrinkItemsInitially(Set<DrinkItems> items) {
        for (DrinkItems i : items) {
            this.drinkItemsList.add(new OrderItemIdDTO(i));
        }
    }
}
