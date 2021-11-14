package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.item.Item;

public class ItemDTOForMenu {
    private Long id;
    private String name;
    private String iconBase64;

    public ItemDTOForMenu(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.iconBase64 = new String(item.getIconBase64());
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

    public String getIconBase64() {
        return iconBase64;
    }

    public void setIconBase64(String iconBase64) {
        this.iconBase64 = iconBase64;
    }
}
