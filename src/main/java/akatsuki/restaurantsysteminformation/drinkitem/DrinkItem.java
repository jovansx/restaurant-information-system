package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "drink_item")
@Getter
@Setter
@NoArgsConstructor
public class DrinkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public DrinkItem(int amount, Item item) {
        this.amount = amount;
        this.item = item;
    }
}
