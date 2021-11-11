package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class DishItem extends OrderItem {

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser chef;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public DishItem() {
    }

    public DishItem(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state, int amount, UnregisteredUser chef, Item item) {
        super(notes, createdAt, isDeleted, state);
        this.amount = amount;
        this.chef = chef;
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UnregisteredUser getChef() {
        return chef;
    }

    public void setChef(UnregisteredUser chef) {
        this.chef = chef;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
