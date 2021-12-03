package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitem.DrinkItem;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.orderitem.OrderItem;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "drink_items")
@Getter
@Setter
@NoArgsConstructor
public class DrinkItems extends OrderItem {

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser bartender;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<DrinkItem> drinkItemList;

    public DrinkItems(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state, UnregisteredUser bartender, List<DrinkItem> drinkItemList, boolean active) {
        super(notes, createdAt, isDeleted, state, active);
        this.bartender = bartender;
        this.drinkItemList = drinkItemList;
    }

    public DrinkItems(Long id, String notes, LocalDateTime createdAt, boolean deleted, ItemState state, boolean active, UnregisteredUser bartender, List<DrinkItem> drinkItemList) {
        super(id, notes, createdAt, deleted, state, active);
        this.bartender = bartender;
        this.drinkItemList = drinkItemList;
    }
}
