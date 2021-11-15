package akatsuki.restaurantsysteminformation.item.dto;

import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import akatsuki.restaurantsysteminformation.price.dto.PriceDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ItemDetailsDTO extends ItemDTO {

    private ItemCategoryDTO itemCategory;
    private List<PriceDTO> prices;

    public ItemDetailsDTO(Item item) {
        super(item.getCode(), item.getName(), item.getDescription(), new String(item.getIconBase64()), item.getType(), item.getComponents());
        this.itemCategory = new ItemCategoryDTO(item.getItemCategory());
        this.prices = item.getPrices().stream().map(PriceDTO::new).collect(Collectors.toList());
    }
}
