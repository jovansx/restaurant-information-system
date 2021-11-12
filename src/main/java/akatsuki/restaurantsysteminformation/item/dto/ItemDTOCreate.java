package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.enums.ItemType;

import java.util.List;

public class ItemDTOCreate {
    private String code;
    private String name;
    private String description;
    private String iconBase64;
    private ItemType type;
    private List<String> components;
    private String itemCategory;
    private int price;

    public ItemDTOCreate(String code, String name, String description, String iconBase64, ItemType type, List<String> components, String itemCategory, int price) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.iconBase64 = iconBase64;
        this.type = type;
        this.components = components;
        this.itemCategory = itemCategory;
        this.price = price;
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

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrices(int price) {
        this.price = price;
    }
}
