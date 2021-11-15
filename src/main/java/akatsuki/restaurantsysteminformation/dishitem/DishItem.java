package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DishItem extends OrderItem {

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser chef;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public DishItem(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state, int amount, UnregisteredUser chef, Item item, boolean active) {
        super(notes, createdAt, isDeleted, state, active);
        this.amount = amount;
        this.chef = chef;
        this.item = item;
    }
}
