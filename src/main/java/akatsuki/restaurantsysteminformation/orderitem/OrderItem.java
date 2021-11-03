package akatsuki.restaurantsysteminformation.orderitem;

import akatsuki.restaurantsysteminformation.enums.ItemState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MyOrderItem")
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

    public OrderItem() {
    }

    public OrderItem(String notes, LocalDateTime createdAt, boolean isDeleted, ItemState state) {
        this.notes = notes;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.state = state;
    }

    public Long getId() {
        return id;
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
