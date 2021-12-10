package akatsuki.restaurantsysteminformation.drinkitem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DrinkItemServiceIntegrationTest {

    @Autowired
    DrinkItemServiceImpl drinkItemService;

//    @Test
//    public void create_ValidObject_SavedObject() {
//        DrinkItem drinkItem = drinkItemService.create(new DrinkItem());
//        Assertions.assertNotNull(drinkItem);
//    }

    @Test
    public void delete_ValidObject_SavedObject() {
        DrinkItem drinkItem = new DrinkItem();
        drinkItem.setId(1L);

        DrinkItem deletedDrinkItem = drinkItemService.delete(drinkItem);
        Assertions.assertNotNull(deletedDrinkItem);
    }
}
