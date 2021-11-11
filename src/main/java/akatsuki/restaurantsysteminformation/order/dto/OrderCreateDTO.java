package akatsuki.restaurantsysteminformation.order.dto;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;

import java.util.List;

public class OrderCreateDTO {
    private Long waiterId;
    private Long createdAt;
    private List<DishItemCreateDTO> dishItemList;
    private List<DrinkItemsCreateDTO> drinkItemsList;

    public OrderCreateDTO() {
    }

    public Long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<DishItemCreateDTO> getDishItemList() {
        return dishItemList;
    }

    public void setDishItemList(List<DishItemCreateDTO> dishItemList) {
        this.dishItemList = dishItemList;
    }

    public List<DrinkItemsCreateDTO> getDrinkItemsList() {
        return drinkItemsList;
    }

    public void setDrinkItemsList(List<DrinkItemsCreateDTO> drinkItemsList) {
        this.drinkItemsList = drinkItemsList;
    }
}
