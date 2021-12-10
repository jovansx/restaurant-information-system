package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private CategoryType type;

    @Column(name = "name", nullable = false)
    private String name;

    public ItemCategory(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }
}
