package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "my_order")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "discarded", nullable = false)
    private boolean discarded;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser waiter;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<DishItem> dishes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<DrinkItems> drinks;

    public Order(double totalPrice, LocalDateTime createdAt, boolean discarded, boolean active, UnregisteredUser waiter, List<DishItem> dishes, List<DrinkItems> drinks) {
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.discarded = discarded;
        this.active = active;
        this.waiter = waiter;
        this.dishes = dishes;
        this.drinks = drinks;
    }
}
