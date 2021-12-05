package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.room.dto.RoomCreateDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Where(clause = "is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<RestaurantTable> restaurantTables;

    public Room(String name, boolean isDeleted, List<RestaurantTable> restaurantTables) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.restaurantTables = restaurantTables;
    }

    public Room(RoomCreateDTO roomDTO) {
        this.name = roomDTO.getName();
        this.isDeleted = false;
        this.restaurantTables = new ArrayList<>();
    }

}
