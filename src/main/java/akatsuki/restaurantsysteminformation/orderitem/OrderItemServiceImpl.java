package akatsuki.restaurantsysteminformation.orderitem;

import akatsuki.restaurantsysteminformation.item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private ItemRepository itemRepository;

    @Autowired
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
