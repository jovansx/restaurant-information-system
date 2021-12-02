package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.dto.ItemCreateDTO;
import akatsuki.restaurantsysteminformation.item.dto.ItemUpdateDTO;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.price.Price;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("CopyConstructorMissesField")
@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "icon_base_64")
    private byte[] iconBase64;

    @Column(name = "original", nullable = false)
    private boolean original;

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

    public Item(String name, String description, byte[] iconBase64, boolean original, boolean deleted, ItemType type, List<String> components, ItemCategory itemCategory, List<Price> prices) {
        this.code = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.iconBase64 = iconBase64;
        this.original = original;
        this.deleted = deleted;
        this.type = type;
        this.components = components;
        this.itemCategory = itemCategory;
        this.prices = prices;
    }

    public Item(ItemCreateDTO itemDTO) {
        this.code = UUID.randomUUID().toString();
        this.name = itemDTO.getName();
        this.description = itemDTO.getDescription();
        this.iconBase64 = itemDTO.getIconBase64().getBytes();
        this.original = false;
        this.deleted = false;
        this.type = itemDTO.getType();
        this.components = new ArrayList<>(itemDTO.getComponents());
        this.itemCategory = new ItemCategory(itemDTO.getItemCategory());
        this.prices = Collections.singletonList(new Price(LocalDateTime.now(), itemDTO.getPrice()));
    }

    public Item(ItemUpdateDTO itemDTO) {
        this.code = itemDTO.getCode();
        this.name = itemDTO.getName();
        this.description = itemDTO.getDescription();
        this.iconBase64 = itemDTO.getIconBase64().getBytes();
        this.original = false;
        this.deleted = false;
        this.type = itemDTO.getType();
        this.components = new ArrayList<>(itemDTO.getComponents());
        this.itemCategory = new ItemCategory(itemDTO.getItemCategory());
        this.prices = Collections.singletonList(new Price(LocalDateTime.now(), itemDTO.getPrice()));
    }

    public Item(Item item) {
        this.code = item.getCode();
        this.name = item.getName();
        this.description = item.getDescription();
        this.iconBase64 = item.getIconBase64();
        this.original = false;
        this.deleted = false;
        this.type = item.getType();
        this.components = new ArrayList<>(item.getComponents());
        this.itemCategory = item.getItemCategory();
        int indexOfLastPrice = item.getPrices().size() - 1;
        Price newPrice = new Price(LocalDateTime.now(), item.getPrices().get(indexOfLastPrice).getValue());
        this.prices = new ArrayList<>();
        this.prices.add(newPrice);
    }

    public double getLastDefinedPrice() {
        return prices.get(prices.size() - 1).getValue();
    }
}
