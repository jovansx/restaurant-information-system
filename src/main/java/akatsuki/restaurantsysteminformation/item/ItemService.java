package akatsuki.restaurantsysteminformation.item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item getOne(Long id);

    List<Item> getAll();

    void create(Item item);

    void update(Item item, long id);

    void delete(long id);

    void saveChanges();

    void discardChanges();

    List<Item> getAllByCategory(String category);
}
