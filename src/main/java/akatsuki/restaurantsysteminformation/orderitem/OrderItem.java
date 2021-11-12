package akatsuki.restaurantsysteminformation.orderitem;

import akatsuki.restaurantsysteminformation.enums.ItemState;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MyOrderItem")
@Where(clause = "is_deleted = false")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "state", nullable = false)
    private ItemState state;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public OrderItem() {
    }

    public OrderItem(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state, boolean isActive) {
        this.notes = notes;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.state = state;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public ItemState getState() {
        return state;
    }

    public void setState(ItemState state) {
        this.state = state;
    }
}
