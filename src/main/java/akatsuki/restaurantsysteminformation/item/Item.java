package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.price.Price;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "icon_base_64", nullable = false)
    private String iconBase64;

    @Column(name = "is_currently_active", nullable = false)
    private boolean isCurrentlyActive;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "type", nullable = false)
    private ItemType type;

    @ElementCollection
    private List<String> components;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemCategory itemCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Price> prices;

    public Item() {
    }

    public Item(Long id, String code, String name, String description, String iconBase64, boolean isCurrentlyActive, boolean isDeleted, ItemType type, List<String> components, ItemCategory itemCategory, List<Price> prices) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.iconBase64 = iconBase64;
        this.isCurrentlyActive = isCurrentlyActive;
        this.isDeleted = isDeleted;
        this.type = type;
        this.components = components;
        this.itemCategory = itemCategory;
        this.prices = prices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconBase64() {
        return iconBase64;
    }

    public void setIconBase64(String iconBase64) {
        this.iconBase64 = iconBase64;
    }

    public boolean isCurrentlyActive() {
        return isCurrentlyActive;
    }

    public void setCurrentlyActive(boolean currentlyActive) {
        isCurrentlyActive = currentlyActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }
}
