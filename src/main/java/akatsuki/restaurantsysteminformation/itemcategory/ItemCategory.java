package akatsuki.restaurantsysteminformation.itemcategory;

import javax.persistence.*;

@Entity
@Table(name = "ItemCategory")
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public ItemCategory() {
    }

    public ItemCategory(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
