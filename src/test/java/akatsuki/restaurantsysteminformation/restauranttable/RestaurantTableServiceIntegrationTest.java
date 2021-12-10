package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
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

    @Autowired
    OrderService orderService;

    @Test
    void getOne_ValidId_ObjectIsReturned() {
        RestaurantTable foundTable = restaurantTableService.getOne(1L);
        Assertions.assertNotNull(foundTable);
        Assertions.assertEquals("T1", foundTable.getName());
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
    void getOneByNameWithOrder_ValidName_ObjectIsReturned() {
        RestaurantTable foundTable = restaurantTableService.getOneByNameWithOrder("T1");
        Assertions.assertNotNull(foundTable);
        Assertions.assertNotNull(foundTable.getActiveOrder());
    }

    @Test
    void getOneByNameWithOrder_NotExistingName_ExceptionThrown() {
        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOneByNameWithOrder("NameExistingName"));
    }

    @Test
    void getOrderByTableName_ValidName_IdIsReturned() {
        Long orderId = restaurantTableService.getOrderByTableName("T1");
        Assertions.assertNotNull(orderId);
        Assertions.assertTrue(orderId > 0);
    }

    @Test
    void getOrderByTableName_TableDoesNotHaveOrder_ReturnedNull() {
        Long orderId = restaurantTableService.getOrderByTableName("T2");
        Assertions.assertNull(orderId);
    }

    @Test
    void getAll_ObjectsExist_ReturnedAsList() {
        List<RestaurantTable> list = restaurantTableService.getAll();
        Assertions.assertEquals(4, list.size());
    }

    @Test
    public void create_ValidObject_SavedObject() {
        RestaurantTable table = new RestaurantTable("T3", TableState.FREE, TableShape.SQUARE, false, null, 0, 0);
        RestaurantTable createdTable = restaurantTableService.create(table, 1L);
        Assertions.assertNotNull(createdTable);
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable("T1", TableState.FREE, TableShape.SQUARE, false, null, 0, 0);
        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.create(table, 1L));
    }

    @Test
    public void update_ValidObject_ObjectIsUpdated() {
        RestaurantTable table = new RestaurantTable("T45", TableState.FREE, TableShape.CIRCLE, false, null, 8, 8);
        RestaurantTable restaurantTable = restaurantTableService.update(table, 2L, 1L);
        Assertions.assertNotNull(restaurantTable);
        Assertions.assertEquals("T45", restaurantTable.getName());
        Assertions.assertEquals(TableShape.CIRCLE, restaurantTable.getShape());
        Assertions.assertEquals(8, restaurantTable.getColumn());
        Assertions.assertEquals(8, restaurantTable.getRow());
    }

    @Test
    public void update_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable("T2", TableState.FREE, TableShape.SQUARE, false, null, 0, 0);
        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.update(table, 1L, 1L));
    }

    @Test
    public void changeStateOfTableWithOrder_InvalidOrder_ExceptionThrown() {
        Order order = orderService.getOneWithAll(10L);
        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.changeStateOfTableWithOrder(order, TableState.TAKEN));
    }

    @Test
    public void changeStateOfTableWithOrder_ValidStateAndOrder_ObjectUpdated() {
        Order order = orderService.getOneWithAll(1L);
        RestaurantTable table = restaurantTableService.changeStateOfTableWithOrder(order, TableState.FREE);

        Assertions.assertNotNull(table);
        Assertions.assertNull(table.getActiveOrder());
        Assertions.assertEquals(TableState.FREE, table.getState());
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

    @Test
    public void setOrderToTable_ValidId_ObjectUpdated() {
        Order order = orderService.getOneWithAll(2L);
        RestaurantTable table = restaurantTableService.setOrderToTable(1L, order);
        Assertions.assertNotNull(table);
        Assertions.assertEquals(order, table.getActiveOrder());
        Assertions.assertEquals(TableState.TAKEN, table.getState());
    }

}