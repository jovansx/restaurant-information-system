package akatsuki.restaurantsysteminformation.price;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price")
@Data
@NoArgsConstructor
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "value", nullable = false)
    private double value;

    public Price(LocalDateTime createdAt, double value) {
        this.createdAt = createdAt;
        this.value = value;
    }
}
