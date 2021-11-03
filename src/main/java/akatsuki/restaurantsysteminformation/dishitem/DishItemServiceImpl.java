package akatsuki.restaurantsysteminformation.dishitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishItemServiceImpl implements DishItemService {
    private DishItemRepository dishItemRepository;

    @Autowired
    public void setDishItemRepository(DishItemRepository dishItemRepository) {
        this.dishItemRepository = dishItemRepository;
    }
}
