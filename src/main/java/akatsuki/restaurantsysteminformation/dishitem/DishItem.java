package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dish_item")
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

    public DishItem(Long id, String notes, LocalDateTime createdAt, boolean deleted, ItemState state, boolean active, int amount, UnregisteredUser chef, Item item) {
        super(id, notes, createdAt, deleted, state, active);
        this.amount = amount;
        this.chef = chef;
        this.item = item;
    }
}
