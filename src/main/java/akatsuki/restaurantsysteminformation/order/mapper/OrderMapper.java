package akatsuki.restaurantsysteminformation.order.mapper;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import java.time.LocalDateTime;
import java.util.List;

public class OrderMapper {

    public static Order convertOrderCreateDTOToOrder(LocalDateTime dateTime,
                                                     List<DishItem> dishItemList, List<DrinkItems> drinkItemsList, UnregisteredUser waiter) {
        return new Order(0, dateTime, false, true, waiter, dishItemList, drinkItemsList);
    }
}
