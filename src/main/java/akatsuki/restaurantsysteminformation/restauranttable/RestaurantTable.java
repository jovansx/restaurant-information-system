package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "restaurant_table")
@Where(clause = "is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "state", nullable = false)
    private TableState state;

    @Column(name = "shape", nullable = false)
    private TableShape shape;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    private Order activeOrder;

    public RestaurantTable(String name, TableState state, TableShape shape, boolean isDeleted, Order activeOrder) {
        this.name = name;
        this.state = state;
        this.shape = shape;
        this.isDeleted = isDeleted;
        this.activeOrder = activeOrder;
    }

    public RestaurantTable(RestaurantTableCreateDTO tableDTO) {
        this.name = tableDTO.getName();
        this.state = TableState.valueOf(tableDTO.getState());
        this.shape = TableShape.valueOf(tableDTO.getShape());
        this.isDeleted = false;
        this.activeOrder = null;
    }
}
