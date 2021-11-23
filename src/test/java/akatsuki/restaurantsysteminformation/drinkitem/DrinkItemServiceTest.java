package akatsuki.restaurantsysteminformation.drinkitem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DrinkItemServiceTest {

    @InjectMocks
    DrinkItemServiceImpl drinkItemService;

    @Mock
    DrinkItemRepository drinkItemRepositoryMock;

    @Test
    @DisplayName("When valid object is passed, new object is created.")
    public void create_ValidObject_SavedObject() {
        drinkItemService.create(new DrinkItem());
        Mockito.verify(drinkItemRepositoryMock, Mockito.times(1)).save(Mockito.any(DrinkItem.class));
    }

    @Test
    @DisplayName("When valid object is passed, new object is deleted.")
    public void delete_ValidObject_SavedObject() {
        drinkItemService.delete(new DrinkItem());
        Mockito.verify(drinkItemRepositoryMock, Mockito.times(1)).delete(Mockito.any(DrinkItem.class));
    }

}
