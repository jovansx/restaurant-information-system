package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableStateNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantTableServiceIntegrationTest {

    @Autowired
    RestaurantTableServiceImpl restaurantTableService;

    @Test
    void getOne_ValidId_ObjectIsReturned() {
        RestaurantTable foundTable = restaurantTableService.getOne(1L);
        Assertions.assertNotNull(foundTable);
    }

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOne(8000L));
    }

    @Test
    void getOneWithOrder_ValidId_ObjectIsReturned() {
        RestaurantTable foundTable = restaurantTableService.getOneWithOrder(1L);
        Assertions.assertNotNull(foundTable);
        Assertions.assertNotNull(foundTable.getActiveOrder());
    }

    @Test
    void getOneWithOrder_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOneWithOrder(8000L));
    }

    @Test
    void getAll_ObjectsExist_ReturnedAsList() {
        List<RestaurantTable> list = restaurantTableService.getAll();
        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    public void create_ValidObject_SavedObject() {
        RestaurantTable table = new RestaurantTable("T3", TableState.FREE, TableShape.SQUARE, false, null);
        RestaurantTable createdTable = restaurantTableService.create(table);
        Assertions.assertNotNull(createdTable);
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable("T1", TableState.FREE, TableShape.SQUARE, false, null);
        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.create(table));
    }

    @Test
    public void update_ValidObject_ObjectIsUpdated() {
        RestaurantTable table = new RestaurantTable("T2", TableState.FREE, TableShape.CIRCLE, false, null);
        RestaurantTable restaurantTable = restaurantTableService.update(table, 2L);
        Assertions.assertNotNull(restaurantTable);
        Assertions.assertEquals(restaurantTable.getShape(), TableShape.CIRCLE);
    }

    @Test
    public void update_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable("T2", TableState.FREE, TableShape.SQUARE, false, null);
        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.update(table, 1L));
    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        RestaurantTable table = restaurantTableService.delete(2L);
        Assertions.assertNotNull(table);
    }

    @Test
    public void getActiveOrderIdByTableId_InvalidState_ExceptionThrown() {
        Assertions.assertThrows(RestaurantTableStateNotValidException.class, () -> restaurantTableService.getActiveOrderIdByTableId(2L));
    }

    @Test
    public void getActiveOrderIdByTableId_ValidId_IdReturned() {
        Long foundId = restaurantTableService.getActiveOrderIdByTableId(1L);
        Assertions.assertNotNull(foundId);
    }

}