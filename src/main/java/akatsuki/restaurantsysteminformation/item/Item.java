package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.price.Price;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private byte[] iconBase64;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Price> prices;

    public Item() {
    }

    public Item(String code, String name, String description, byte[] iconBase64, boolean isCurrentlyActive, boolean isDeleted, ItemType type, List<String> components, ItemCategory itemCategory, List<Price> prices) {
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

    public byte[] getIconBase64() {
        return iconBase64;
    }

    public void setIconBase64(byte[] iconBase64) {
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

    public Price getLastDefinedPrice() {
        if (prices.size() == 0) {
            return null;
        }
        if (prices.size() > 1) {
            List<Price> sortedPrices = prices.stream().sorted(Comparator.comparingDouble(Price::getValue)).collect(Collectors.toList());
            Collections.reverse(sortedPrices);
            return sortedPrices.get(0);
        }
        return prices.get(0);
    }
}
