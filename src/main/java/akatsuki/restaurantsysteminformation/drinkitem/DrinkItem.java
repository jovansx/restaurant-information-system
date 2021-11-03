package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.item.Item;

import javax.persistence.*;

@Entity
@Table(name = "DrinkItem")
public class DrinkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public DrinkItem() {
    }

    public DrinkItem(Long id, int amount, Item item) {
        this.id = id;
        this.amount = amount;
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
