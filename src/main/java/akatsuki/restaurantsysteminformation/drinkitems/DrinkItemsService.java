package akatsuki.restaurantsysteminformation.drinkitems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrinkItemsService {
    private DrinkItemsRepository drinkItemsRepository;

    @Autowired
    public void setDrinkItemsRepository(DrinkItemsRepository drinkItemsRepository) {
        this.drinkItemsRepository = drinkItemsRepository;
    }
}
