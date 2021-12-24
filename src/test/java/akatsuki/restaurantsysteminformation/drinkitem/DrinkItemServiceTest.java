package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitem.exception.DrinkItemNotFoundException;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemRepository;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DrinkItemServiceTest {

    @InjectMocks
    DrinkItemServiceImpl drinkItemService;

    @Mock
    DrinkItemRepository drinkItemRepositoryMock;

    @Mock
    ItemRepository itemRepositoryMock;

    @Test
    public void findByIdAndFetchItem_Valid_ReturnObject() {
        DrinkItem drinkItem = new DrinkItem();
        Mockito.when(drinkItemRepositoryMock.findByIdAndFetchItem(1L)).thenReturn(Optional.of(drinkItem));
        DrinkItem foundDrinkItem = drinkItemService.findByIdAndFetchItem(1L);
        Assertions.assertEquals(drinkItem, foundDrinkItem);
    }

    @Test
    public void findByIdAndFetchItem_InvalidId_ExceptionThrown() {
        Mockito.when(drinkItemRepositoryMock.findByIdAndFetchItem(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DrinkItemNotFoundException.class,
                () -> drinkItemService.findByIdAndFetchItem(8000L));
    }

    @Test
    public void create_ValidObject_SavedObject() {
        Item item = new Item();
        Mockito.when(itemRepositoryMock.findById(1l)).thenReturn(Optional.of(item));
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 1L, 1L, 3);
        drinkItemService.create(drinkItemUpdateDTO);
        Mockito.verify(drinkItemRepositoryMock, Mockito.times(1)).save(Mockito.any(DrinkItem.class));
    }

    @Test
    public void create_InvalidItemId_ExceptionThrown() {
        Mockito.when(itemRepositoryMock.findById(8000L)).thenReturn(Optional.empty());
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 8000L, 1L, 3);
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> drinkItemService.create(drinkItemUpdateDTO));
    }

    @Test
    public void update_ValidObject_SavedObject() {
        DrinkItem drinkItem = new DrinkItem();
        Mockito.when(drinkItemRepositoryMock.findById(1L)).thenReturn(Optional.of(drinkItem));
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 1L, 1L, 3);
        drinkItemService.update(drinkItemUpdateDTO, 1L);
        Mockito.verify(drinkItemRepositoryMock, Mockito.times(1)).save(drinkItem);
    }

    @Test
    public void update_InvalidDrinkItemId_ExceptionThrown() {
        Mockito.when(drinkItemRepositoryMock.findById(8000L)).thenReturn(Optional.empty());
        DrinkItemUpdateDTO drinkItemUpdateDTO = new DrinkItemUpdateDTO(20, 1L, 8000L, 3);
        Assertions.assertThrows(DrinkItemNotFoundException.class,
                () -> drinkItemService.update(drinkItemUpdateDTO, 8000L));
    }

    @Test
    public void delete_InvalidId_ExceptionThrown() {
        Mockito.when(drinkItemRepositoryMock.findById(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DrinkItemNotFoundException.class,
                () -> drinkItemService.delete(8000L));
    }

}
