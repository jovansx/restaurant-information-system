package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsInvalidStateException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotContainedException;
import akatsuki.restaurantsysteminformation.drinkitems.exception.DrinkItemsNotFoundException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DrinkItemsServiceIntegrationTest {

    @Autowired
    DrinkItemsServiceImpl drinkItemsService;

    @Test
    public void findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered_ValidId_ReturnedObject() {
        DrinkItems drinkItems = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(6L);
        Assertions.assertNotNull(drinkItems);
    }

    @Test
    public void findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(8000L));
    }

    @Test
    public void findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew_ValidId_ReturnedObject() {
        DrinkItems drinkItems = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(6L);
        Assertions.assertNotNull(drinkItems);
    }

    @Test
    public void findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(8000L));
    }

    @Test
    public void findOneActiveAndFetchItemAndChef_ValidId_ReturnedObject() {
        DrinkItems foundDrinkItems = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNewOrDelivered(5L);
        Assertions.assertNotNull(foundDrinkItems);
    }

    @Test
    void findAllActiveAndFetchBartenderAndItems_DrinkItemsExist_ReturnedList() {
        List<DrinkItems> foundList = drinkItemsService.findAllActiveAndFetchBartenderAndItems();
        Assertions.assertEquals(foundList.size(), 4);
    }

//    @Test
//    public void create_ValidDto_SavedObject() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, list, "Notes");
//
//        DrinkItems drinkItems = drinkItemsService.create(drinkItemsCreateDTO);
//        Assertions.assertNotNull(drinkItems);
//    }

//    @Test
//    public void create_InvalidItemTypeDto_ExceptionThrown() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 4L));
//        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, list, "Notes");
//
//        Assertions.assertThrows(ItemNotFoundException.class, () -> drinkItemsService.create(drinkItemsCreateDTO));
//    }

//    @Test
//    public void update_ValidDtoAndId_ChangedObject() {
//        List<DrinkItemUpdateDTO> list = Collections.singletonList(new DrinkItemUpdateDTO(14, 1L));
//        DrinkItemsUpdateDTO dto = new DrinkItemsUpdateDTO(1, list, "Notes");
//
//        DrinkItems drinkitems = drinkItemsService.update(dto, 6L);
//        Assertions.assertEquals(drinkitems.getDrinkItemList().size(), 1);
//        Assertions.assertEquals(drinkitems.getDrinkItemList().get(0).getAmount(), 14);
//        Assertions.assertEquals(drinkitems.getNotes(), "Notes");
//    }

//    @Test
//    public void update_InvalidItemState_ExceptionThrown() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, list, "Notes");
//
//        Assertions.assertThrows(DrinkItemsInvalidStateException.class, () -> drinkItemsService.update(dto, 5L));
//    }

//    @Test
//    public void update_InvalidOrderId_ExceptionThrown() {
//        List<DrinkItemCreateDTO> list = Collections.singletonList(new DrinkItemCreateDTO(1, 1L));
//        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(5, list, "Notes");
//
//        Assertions.assertThrows(DrinkItemsNotContainedException.class, () -> drinkItemsService.update(dto, 5L));
//    }

    @Test
    void changeStateOfDrinkItems_ValidIds_FromOnHoldToPreparation() {
        DrinkItems changedDrinkItems = drinkItemsService.changeStateOfDrinkItems(6L, 2L);
        Assertions.assertEquals(changedDrinkItems.getState(), ItemState.PREPARATION);
    }

    @Test
    void changeStateOfDrinkItems_ValidIds_FromPreparationToReady() {
        DrinkItems changedDrinkItems = drinkItemsService.changeStateOfDrinkItems(5L, 2L);
        Assertions.assertEquals(changedDrinkItems.getState(), ItemState.READY);
    }

    @Test
    void changeStateOfDrinkItems_ValidIds_FromReadyToDelivered() {
        DrinkItems changedDrinkItems = drinkItemsService.changeStateOfDrinkItems(8L, 1L);
        Assertions.assertEquals(changedDrinkItems.getState(), ItemState.DELIVERED);
    }

    @Test
    void changeStateOfDrinkItems_InvalidItemId_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.changeStateOfDrinkItems(1000L, 1L));
    }

    @Test
    void changeStateOfDrinkItems_InvalidItemState_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.changeStateOfDrinkItems(9L, 1L));
    }

    @Test
    void changeStateOfDrinkItems_NotAllowedUserType_ExceptionThrown() {
        Assertions.assertThrows(UserNotFoundException.class, () -> drinkItemsService.changeStateOfDrinkItems(5L, 1L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        DrinkItems drinkItems = drinkItemsService.delete(6L);

        Assertions.assertTrue(drinkItems.isDeleted());
        Assertions.assertFalse(drinkItems.isActive());
    }

    @Test
    public void delete_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemsNotFoundException.class, () -> drinkItemsService.delete(1L));
    }

    @Test
    public void delete_InvalidState_ExceptionThrown() {
        Assertions.assertThrows(DrinkItemsInvalidStateException.class, () -> drinkItemsService.delete(5L));
    }

    @Test
    void isBartenderActive_ContainsUser_False() {
        UnregisteredUser user = new UnregisteredUser();
        user.setId(2L);

        boolean isActive = drinkItemsService.isBartenderActive(user);
        Assertions.assertFalse(isActive);
    }

    @Test
    void isBartenderActive_ContainsUser_True() {
        UnregisteredUser user = new UnregisteredUser();
        user.setId(5L);

        boolean isActive = drinkItemsService.isBartenderActive(user);
        Assertions.assertTrue(isActive);
    }
}