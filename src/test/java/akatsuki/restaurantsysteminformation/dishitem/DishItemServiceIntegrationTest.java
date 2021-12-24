package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DishItemServiceIntegrationTest {

    @Autowired
    DishItemServiceImpl dishItemService;

    @Test
    public void findOneActiveAndFetchItemAndChef_NegativeId_ExceptionThrown() {
        Assertions.assertThrows(DishItemNotFoundException.class,
                () -> dishItemService.findOneActiveAndFetchItemAndChef(8000L));
    }

    @Test
    public void findOneActiveAndFetchItemAndChef_ValidId_ReturnedObject() {
        DishItem foundDishItem = dishItemService.findOneActiveAndFetchItemAndChef(2L);
        Assertions.assertTrue(foundDishItem.isActive());
        Assertions.assertNotNull(foundDishItem.getChef());
        Assertions.assertNotNull(foundDishItem.getItem());
    }

    @Test
    void findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered_DishItemsExist_ReturnedList() {
        List<DishItem> foundList = dishItemService.findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();
        Assertions.assertEquals(foundList.size(), 3);
    }

//    @Test
//    public void create_ValidDto_SavedObject() {
//        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(4L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
//        DishItem dishItem = dishItemService.create(dishItemCreateDTO);
//        Assertions.assertNotNull(dishItem);
//    }
//
//    @Test
//    public void create_InvalidItemType_ExceptionThrown() {
//        DishItemCreateDTO dishItemCreateDTO = new DishItemCreateDTO(1L, 10, "Give me the coldest beer that you have. I'll give you a good tip.", 1L);
//        Assertions.assertThrows(DishItemInvalidTypeException.class, () -> dishItemService.create(dishItemCreateDTO));
//    }

//    @Test
//    public void update_ValidDtoAndId_ChangedObject() {
//        DishItemCreateDTO dto = new DishItemCreateDTO(4L, 7, "New note.", 1L);
//        DishItem dishItem = dishItemService.update(dto, 1L);
//
//        Assertions.assertEquals(dishItem.getAmount(), 7);
//        Assertions.assertEquals(dishItem.getItem().getId(), 4L);
//        Assertions.assertEquals(dishItem.getNotes(), "New note.");
//    }

//    @Test
//    public void update_InvalidItemType_ExceptionThrown() {
//        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 10, "New note.", 1L);
//        Assertions.assertThrows(DishItemInvalidTypeException.class, () -> dishItemService.update(dto, 1L));
//    }
//
//    @Test
//    public void update_InvalidDishItem_ExceptionThrown() {
//        DishItemCreateDTO dto = new DishItemCreateDTO(4L, 10, "New note.", 2L);
//        Assertions.assertThrows(DishItemOrderException.class, () -> dishItemService.update(dto, 1L));
//    }
//
//    @Test
//    public void update_InvalidItemState_ExceptionThrown() {
//        DishItemCreateDTO dto = new DishItemCreateDTO(4L, 10, "New note.", 1L);
//        Assertions.assertThrows(DishItemInvalidStateException.class, () -> dishItemService.update(dto, 2L));
//    }

    @Test
    void changeStateOfDishItem_FromOnHoldToPreparation_ChangedState() {
        DishItem changedDishItem = dishItemService.changeStateOfDishItems(1L, 3L);
        Assertions.assertEquals(changedDishItem.getState(), ItemState.PREPARATION);
    }

    @Test
    void changeStateOfDishItem_FromReadyToDelivered_ChangedState() {
        DishItem changedDishItem = dishItemService.changeStateOfDishItems(3L, 1L);
        Assertions.assertEquals(changedDishItem.getState(), ItemState.DELIVERED);
    }

    @Test
    void changeStateOfDishItems_FromPreparationToReady_ChangedState() {
        DishItem changedDishItem = dishItemService.changeStateOfDishItems(2L, 3L);
        Assertions.assertEquals(changedDishItem.getState(), ItemState.READY);
    }

    @Test
    void changeStateOfDishItems_InvalidItemId_ExceptionThrown() {
        Assertions.assertThrows(DishItemNotFoundException.class, () -> dishItemService.changeStateOfDishItems(1000L, 1L));
    }

    @Test
    void changeStateOfDishItems_NotAllowedUserType_ExceptionThrown() {
        Assertions.assertThrows(UserTypeNotValidException.class, () -> dishItemService.changeStateOfDishItems(2L, 1L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        DishItem dishItem = dishItemService.delete(1L);

        Assertions.assertTrue(dishItem.isDeleted());
        Assertions.assertFalse(dishItem.isActive());
    }

    @Test
    public void delete_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(DishItemNotFoundException.class, () -> dishItemService.delete(1000L));
    }

    @Test
    void isChefActive_ContainsUser_False() {
        UnregisteredUser user = new UnregisteredUser();
        user.setId(3L);
        boolean isActive = dishItemService.isChefActive(user);
        Assertions.assertFalse(isActive);
    }

    @Test
    void isChefActive_ContainsUser_True() {
        UnregisteredUser user = new UnregisteredUser();
        user.setId(6L);

        boolean isActive = dishItemService.isChefActive(user);
        Assertions.assertTrue(isActive);
    }
}