package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DrinkItems")
@Where(clause = "is_deleted = false")
public class DrinkItems extends OrderItem {

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser bartender;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<DrinkItem> drinkItemList;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public DrinkItems() {
    }

    public DrinkItems(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state, UnregisteredUser bartender, List<DrinkItem> drinkItems, boolean active) {
        super(notes, createdAt, isDeleted, state);
        this.bartender = bartender;
        this.drinkItemList = drinkItems;
        this.isActive = active;
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

    public List<DrinkItem> getDrinkItemList() {
        return drinkItemList;
    }

    public void setDrinkItemList(List<DrinkItem> drinkItemList) {
        this.drinkItemList = drinkItemList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
