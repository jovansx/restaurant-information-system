package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MyOrder")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_discarded", nullable = false)
    private boolean isDiscarded;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser waiter;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<DishItem> dishes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<DrinkItems> drinks;

    public Order(double totalPrice, LocalDateTime createdAt, boolean isDiscarded, boolean isActive, UnregisteredUser waiter, List<DishItem> dishes, List<DrinkItems> drinks) {
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.isDiscarded = isDiscarded;
        this.isActive = isActive;
        this.waiter = waiter;
        this.dishes = dishes;
        this.drinks = drinks;
    }
}
