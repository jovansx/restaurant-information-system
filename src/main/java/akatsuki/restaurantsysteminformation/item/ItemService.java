package akatsuki.restaurantsysteminformation.item;

import java.util.List;

public interface ItemService {
    Item getOne(Long id);

    Item getOneActive(Long id);

    List<Item> getAllActive();

    List<Item> getAllActiveByCategory(String category);

    void create(Item item);

    void update(Item item, long id);

    void delete(long id);

    void saveChanges();

    void discardChanges();

    double getCurrentPriceOfItem(Long itemId);
}
