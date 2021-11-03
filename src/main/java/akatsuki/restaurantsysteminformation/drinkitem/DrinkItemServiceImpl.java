package akatsuki.restaurantsysteminformation.drinkitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrinkItemServiceImpl implements DrinkItemService {
    private DrinkItemRepository drinkItemRepository;

    @Autowired
    public void setDrinkItemRepository(DrinkItemRepository drinkItemRepository) {
        this.drinkItemRepository = drinkItemRepository;
    }
}
