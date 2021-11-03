package akatsuki.restaurantsysteminformation.price;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Price")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "value", nullable = false)
    private double value;

    public Price() {
    }

    public Price(Long id, LocalDateTime createdAt, double value) {
        this.id = id;
        this.createdAt = createdAt;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
