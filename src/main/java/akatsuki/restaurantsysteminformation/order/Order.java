package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "MyOrder")
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
    private Set<DishItem> dishes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<DrinkItems> drinks;

    public Order() {
    }

    public Order(double totalPrice, LocalDateTime createdAt, boolean discarded, boolean active, UnregisteredUser waiter, Set<DishItem> dishes, Set<DrinkItems> drinks) {
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.discarded = discarded;
        this.active = active;
        this.waiter = waiter;
        this.dishes = dishes;
        this.drinks = drinks;
    }

    public Long getId() {
        return id;
    }

    public Set<DishItem> getDishes() {
        return dishes;
    }

    public void setDishes(Set<DishItem> dishes) {
        this.dishes = dishes;
    }

    public Set<DrinkItems> getDrinks() {
        return drinks;
    }

    public void setDrinks(Set<DrinkItems> drinks) {
        this.drinks = drinks;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UnregisteredUser getWaiter() {
        return waiter;
    }

    public void setWaiter(UnregisteredUser waiter) {
        this.waiter = waiter;
    }
}
