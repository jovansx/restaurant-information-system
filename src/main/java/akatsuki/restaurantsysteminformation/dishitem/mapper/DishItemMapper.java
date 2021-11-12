package akatsuki.restaurantsysteminformation.dishitem.mapper;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.item.Item;

import java.time.LocalDateTime;

public class DishItemMapper {

    public static DishItem convertDishItemCreateDTOToDishItem(DishItemCreateDTO dishItemCreateDTO, LocalDateTime dateTime, Item item) {
        return new DishItem(
                dishItemCreateDTO.getNotes(),
                dateTime,
                false,
                ItemState.NEW,
                dishItemCreateDTO.getAmount(),
                null,
                item,
                true
        );
    }
}
