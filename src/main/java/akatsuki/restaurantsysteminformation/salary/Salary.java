package akatsuki.restaurantsysteminformation.salary;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary")
@Data
@NoArgsConstructor
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "value", nullable = false)
    private double value;

    public Salary(LocalDateTime createdAt, double value) {
        this.createdAt = createdAt;
        this.value = value;
    }
}
