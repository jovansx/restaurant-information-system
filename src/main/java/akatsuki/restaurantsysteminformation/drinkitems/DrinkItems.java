package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DrinkItems")
public class DrinkItems extends OrderItem {

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser bartender;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<DrinkItem> drinkItemList;

    public DrinkItems() {
    }

    public DrinkItems(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state, UnregisteredUser bartender, List<DrinkItem> drinkItems) {
        super(notes, createdAt, isDeleted, state);
        this.bartender = bartender;
        this.drinkItemList = drinkItems;
    }

    public UnregisteredUser getBartender() {
        return bartender;
    }

    public void setBartender(UnregisteredUser bartender) {
        this.bartender = bartender;
    }

    public List<DrinkItem> getDrinkItems() {
        return drinkItemList;
    }

    public void setDrinkItems(List<DrinkItem> drinkItems) {
        this.drinkItemList = drinkItems;
    }
}
