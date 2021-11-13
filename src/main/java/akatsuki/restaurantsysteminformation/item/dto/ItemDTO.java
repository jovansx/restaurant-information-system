package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import akatsuki.restaurantsysteminformation.price.dto.PriceDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ItemDTO {

    private String code;
    private String name;
    private String description;
    private String iconBase64;
    private ItemType type;
    private List<String> components;
    private ItemCategoryDTO itemCategory;
    private List<PriceDTO> prices;

    public ItemDTO(Item item) {
        this.code = item.getCode();
        this.name = item.getName();
        this.description = item.getDescription();
        this.iconBase64 = new String(item.getIconBase64());
        this.type = item.getType();
        this.components = item.getComponents();
        this.itemCategory = new ItemCategoryDTO(item.getItemCategory());
        this.prices = item.getPrices().stream().map(PriceDTO::new).collect(Collectors.toList());
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

    public ItemCategoryDTO getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategoryDTO itemCategory) {
        this.itemCategory = itemCategory;
    }

    public List<PriceDTO> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceDTO> prices) {
        this.prices = prices;
    }
}
