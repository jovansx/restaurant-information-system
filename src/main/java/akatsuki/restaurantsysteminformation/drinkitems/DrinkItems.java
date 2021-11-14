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

    public DrinkItems(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state, UnregisteredUser bartender, List<DrinkItem> drinkItemList, boolean active) {
        super(notes, createdAt, isDeleted, state, active);
        this.bartender = bartender;
        this.drinkItemList = drinkItemList;
    }

    public UnregisteredUser getBartender() {
        return bartender;
    }

    public void setBartender(UnregisteredUser bartender) {
        this.bartender = bartender;
    }

    public List<DrinkItem> getDrinkItemList() {
        return drinkItemList;
    }

    public void setDrinkItemList(List<DrinkItem> drinkItemList) {
        this.drinkItemList = drinkItemList;
    }

}
