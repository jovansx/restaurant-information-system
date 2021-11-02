package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "is_discarded", nullable = false)
    private boolean isDiscarded;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private UnregisteredUser waiter;
}
