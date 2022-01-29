package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitem.exception.DrinkItemNotFoundException;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
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

    @Test
    public void findByIdAndFetchItem_Valid_ReturnObject() {
        DrinkItem foundDrinkItem = drinkItemService.findByIdAndFetchItem(1L);
        Assertions.assertNotNull(foundDrinkItem.getItem());
    }

    @Test
    public void findByIdAndFetchItem_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemNotFoundException.class,
                () -> drinkItemService.findByIdAndFetchItem(8000L));
    }

    @Test
    public void create_ValidObject_SavedObject() {
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 1L, 1L, 3);
        DrinkItem drinkItem = drinkItemService.create(drinkItemUpdateDTO);
        Assertions.assertNotNull(drinkItem);
    }

    @Test
    public void create_InvalidItemId_ExceptionThrown() {
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 8000L, 1L, 3);
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> drinkItemService.create(drinkItemUpdateDTO));
    }

    @Test
    public void update_ValidObject_SavedObject() {
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 1L, 1L, 3);
        DrinkItem drinkItem = drinkItemService.update(drinkItemUpdateDTO, 1L);
        Assertions.assertNotNull(drinkItem);
        Assertions.assertEquals(20, drinkItem.getAmount());
    }

    @Test
    public void update_InvalidDrinkItemId_ExceptionThrown() {
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 1L, 8000L, 3);
        Assertions.assertThrows(DrinkItemNotFoundException.class,
                () -> drinkItemService.update(drinkItemUpdateDTO, 8000L));
    }

    @Test
    public void delete_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemNotFoundException.class,
                () -> drinkItemService.delete(8000L));
    }
}
