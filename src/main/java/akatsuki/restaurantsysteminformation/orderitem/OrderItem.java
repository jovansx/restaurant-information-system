package akatsuki.restaurantsysteminformation.orderitem;

import akatsuki.restaurantsysteminformation.enums.ItemState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "my_order_item")
@Where(clause = "deleted = false")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class OrderItem {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "state", nullable = false)
    private ItemState state;

    @Column(name = "active", nullable = false)
    private boolean active;

    public OrderItem(String notes, LocalDateTime createdAt, boolean deleted, ItemState state, boolean active) {
        this.notes = notes;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.state = state;
        this.active = active;
    }

    public OrderItem(Long id, String notes, LocalDateTime createdAt, boolean deleted, ItemState state, boolean active) {
        this.id = id;
        this.notes = notes;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.state = state;
        this.active = active;
    }
}
