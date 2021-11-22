package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidTypeException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemServiceImpl;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DishItemServiceTest {

    @InjectMocks
    DishItemServiceImpl dishItemService;

    @Mock
    DishItemRepository dishItemRepositoryMock;

    @Mock
    OrderServiceImpl orderServiceMock;

    @Mock
    ItemServiceImpl itemServiceMock;

    @Test
    @DisplayName("When invalid id is passed, exception should occur.")
    public void findOneActiveAndFetchItemAndChef_NegativeId_ExceptionThrown() {
        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChef(-1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DishItemNotFoundException.class, () -> {
            dishItemService.findOneActiveAndFetchItemAndChef(-1);
        });
    }

    @Test
    @DisplayName("When valid id is passed, required object is returned.")
    public void findOneActiveAndFetchItemAndChef_ValidId_ReturnedObject() {
        DishItem dishItem = new DishItem();

        Mockito.when(dishItemRepositoryMock.findOneActiveAndFetchItemAndChef(1L)).thenReturn(Optional.of(dishItem));
        DishItem foundDishItem = dishItemService.findOneActiveAndFetchItemAndChef(1L);

        Assertions.assertEquals(foundDishItem, dishItem);
    }

    //    @Test
//    void findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered() {
//    }
//
    @Test
    @DisplayName("When valid id is passed, required object is returned.")
    public void create_Valid_SavedObject() {
        //TODO: Vidi dal vratiti neki custom dish item kod save
        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(1L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DISH, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        dishItemService.create(dishItemCreateDTO);

        Assertions.assertEquals(order.getDishes().size(), 1);
        Mockito.verify(dishItemRepositoryMock, Mockito.times(1)).save(Mockito.any(DishItem.class));
        Mockito.verify(orderServiceMock, Mockito.times(1)).updateTotalPriceAndSave(order);
    }

    @Test
    @DisplayName("When invalid item type is passed, exception should occur.")
    public void create_InvalidItemType_ExceptionThrown() {
        //TODO: Vidi dal vratiti neki custom dish item kod save
        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(1L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
        Order order = new Order(400, LocalDateTime.now(), false, true, null, new ArrayList<>(), new ArrayList<>());
        Item item = new Item("Chocolate", "Creamy",
                null, true, false, ItemType.DRINK, new ArrayList<>(), new ItemCategory("dessert"), new ArrayList<>());

        Mockito.when(orderServiceMock.getOneWithAll(1L)).thenReturn(order);
        Mockito.when(itemServiceMock.getOne(1L)).thenReturn(item);

        Assertions.assertThrows(DishItemInvalidTypeException.class, () -> {
            dishItemService.create(dishItemCreateDTO);
        });
    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void changeStateOfDishItems() {
//    }
//
//    @Test
//    void delete() {
//    }
//
//    @Test
//    void isChefActive() {
//    }
}