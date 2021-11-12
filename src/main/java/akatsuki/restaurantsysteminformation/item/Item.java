package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.dto.ItemDTOCreate;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.price.Price;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private byte[] iconBase64;

    @Column(name = "currently_active", nullable = false)
    private boolean currentlyActive;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

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

    public Item(String code, String name, String description, byte[] iconBase64, boolean currentlyActive, boolean deleted, ItemType type, List<String> components, ItemCategory itemCategory, List<Price> prices) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.iconBase64 = iconBase64;
        this.currentlyActive = currentlyActive;
        this.deleted = deleted;
        this.type = type;
        this.components = components;
        this.itemCategory = itemCategory;
        this.prices = prices;
    }

    public Item(ItemDTOCreate itemDTO) {
        this.code = itemDTO.getCode();
        this.name = itemDTO.getName();
        this.description = itemDTO.getDescription();
        this.iconBase64 = itemDTO.getIconBase64().getBytes();
        this.currentlyActive = false;
        this.deleted = false;
        this.type = itemDTO.getType();
        this.components = itemDTO.getComponents();
        this.itemCategory = new ItemCategory(itemDTO.getItemCategory());
        this.prices = Collections.singletonList(new Price(LocalDateTime.now(), itemDTO.getPrice()));
    }

    public Item(Item item) {
        this.code = item.getCode();
        this.name = item.getName();
        this.description = item.getDescription();
        this.iconBase64 = item.getIconBase64();
        this.currentlyActive = false;
        this.deleted = false;
        this.type = item.getType();
        this.components = new ArrayList<>(item.getComponents());
        this.itemCategory = item.getItemCategory();
        int indexOfLastPrice = item.getPrices().size()-1;
        Price newPrice = new Price(LocalDateTime.now(), item.getPrices().get(indexOfLastPrice).getValue());
        this.prices = new ArrayList<>();
        this.prices.add(newPrice);
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
        return currentlyActive;
    }

    public void setCurrentlyActive(boolean currentlyActive) {
        this.currentlyActive = currentlyActive;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
