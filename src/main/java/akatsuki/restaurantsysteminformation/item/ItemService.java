package akatsuki.restaurantsysteminformation.item;

import java.util.List;

public interface ItemService {
    Item getOne(Long id);

    Item getOneById(Long id);

    Item getOneActive(Long id);

    List<Item> getAllActive();

    List<Item> getAll();

    List<Item> getAllActiveByCategory(String category);

    Item create(Item item);

    Item update(Item item, long id);

    Item delete(long id);

    void deleteForTesting(long id);

    void saveChanges();

    void save(Item item);

    void discardChanges();

    double getCurrentPriceOfItem(Long itemId);
}
