package akatsuki.restaurantsysteminformation.orderitem;

import akatsuki.restaurantsysteminformation.enums.ItemState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MyOrderItem")
@Where(clause = "deleted = false")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public abstract class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        return id != null ? id.equals(orderItem.id) : orderItem.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
