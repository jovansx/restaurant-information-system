package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RestaurantTable> restaurantTables;

    public Room() {
    }

    public Room(Long id, String name, boolean isDeleted, List<RestaurantTable> restaurantTables) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.restaurantTables = restaurantTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<RestaurantTable> getRestaurantTables() {
        return restaurantTables;
    }

    public void setRestaurantTables(List<RestaurantTable> restaurantTables) {
        this.restaurantTables = restaurantTables;
    }
}
