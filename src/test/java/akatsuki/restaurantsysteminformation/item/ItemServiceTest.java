package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.itemcategory.ItemCategoryService;
import akatsuki.restaurantsysteminformation.price.PriceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    ItemService itemService;
    @Mock
    ItemRepository itemRepositoryMock;
    @Mock
    ItemCategoryService itemCategoryServiceMock;
    @Mock
    PriceService priceServiceMock;

    @Test
    @DisplayName("When invalid id is passed, exception should occur.")
    void getOne() {

    }
}