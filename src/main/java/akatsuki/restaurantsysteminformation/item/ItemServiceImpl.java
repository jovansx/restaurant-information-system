package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;

    @Autowired
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item getOne(Long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Item with the id " + id + " is not found in the database."));
    }
}
