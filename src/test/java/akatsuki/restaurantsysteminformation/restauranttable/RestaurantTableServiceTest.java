package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableStateNotValidException;
import akatsuki.restaurantsysteminformation.room.Room;
import akatsuki.restaurantsysteminformation.room.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RestaurantTableServiceTest {

    @InjectMocks
    RestaurantTableServiceImpl restaurantTableService;
    @Mock
    RestaurantTableRepository restaurantTableRepositoryMock;
    @Mock
    RoomService roomServiceMock;

//    TODO proveri zasto nece da mockuje room service

    @Test
    void getOne_ValidId_ObjectIsReturned() {
        RestaurantTable table = new RestaurantTable();

        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(table));
        RestaurantTable foundTable = restaurantTableService.getOne(1L);

        Assertions.assertEquals(foundTable, table);
    }

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(restaurantTableRepositoryMock.findById(8000L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOne(8000L));
    }

    @Test
    void getOneWithOrder_ValidId_ObjectIsReturned() {
        RestaurantTable table = new RestaurantTable();

        Mockito.when(restaurantTableRepositoryMock.findByIdAndFetchOrder(1L)).thenReturn(Optional.of(table));
        RestaurantTable foundTable = restaurantTableService.getOneWithOrder(1L);
        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOne(8000L));

        Assertions.assertEquals(foundTable, table);
    }

    @Test
    void getOneWithOrder_InvalidId_ExceptionThrown() {
        Mockito.when(restaurantTableRepositoryMock.findByIdAndFetchOrder(8000L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOneWithOrder(8000L));
    }

    @Test
    void getOneByNameWithOrder_ValidName_ObjectReturned() {
        Mockito.when(restaurantTableRepositoryMock.findByNameAndFetchOrder("T1")).thenReturn(Optional.of(new RestaurantTable()));

        RestaurantTable table = restaurantTableService.getOneByNameWithOrder("T1");
        Assertions.assertNotNull(table);
    }

    @Test
    void getOneByNameWithOrder_InvalidName_ExceptionThrown() {
        Mockito.when(restaurantTableRepositoryMock.findByNameAndFetchOrder("InvalidName")).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOneByNameWithOrder("InvalidName"));
    }

    @Test
    void getOrderByTableName_ValidName_OrderIdReturned() {
        Order order = new Order();
        order.setId(1L);

        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setActiveOrder(order);

        Mockito.when(restaurantTableRepositoryMock.findByNameAndFetchOrder("T1")).thenReturn(Optional.of(restaurantTable));

        Long orderId = restaurantTableService.getOrderByTableName("T1");

        Assertions.assertEquals(1L, orderId);
    }

    @Test
    void getOrderByTableName_OrderNotExistOnThatTable_NullReturned() {
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setActiveOrder(null);

        Mockito.when(restaurantTableRepositoryMock.findByNameAndFetchOrder("T1")).thenReturn(Optional.of(restaurantTable));

        Long orderId = restaurantTableService.getOrderByTableName("T1");

        Assertions.assertNull(orderId);
    }

    @Test
    void getAll_ObjectsExist_ReturnedAsList() {
        Mockito.when(restaurantTableRepositoryMock.findAll()).thenReturn(List.of(new RestaurantTable(), new RestaurantTable()));

        List<RestaurantTable> list = restaurantTableService.getAll();

        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    public void setOrderToTable_ValidIds_ObjectUpdated() {
        Order order = new Order();
        order.setId(1L);

        RestaurantTable table = new RestaurantTable();

        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(table));

        restaurantTableService.setOrderToTable(1L, order);

        Assertions.assertEquals(1L, table.getActiveOrder().getId());
        Assertions.assertEquals(TableState.TAKEN, table.getState());

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(table);
    }

    @Test
    public void changeStateOfTableWithOrder_ValidObjectIsPassed_ObjectUpdated() {
        Order order = new Order();
        order.setId(1L);

        RestaurantTable table = new RestaurantTable();

        Mockito.when(restaurantTableRepositoryMock.findByActiveOrder(order)).thenReturn(Optional.of(table));

        restaurantTableService.changeStateOfTableWithOrder(order, TableState.FREE);

        Assertions.assertEquals(TableState.FREE, table.getState());
        Assertions.assertNull(table.getActiveOrder());

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(table);
    }

    @Test
    public void changeStateOfTableWithOrder_OrderNotOnTable_ExceptionThrown() {
        Order order = new Order();
        order.setId(1L);

        Mockito.when(restaurantTableRepositoryMock.findByActiveOrder(order)).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () ->  restaurantTableService.changeStateOfTableWithOrder(order, TableState.TAKEN));
    }

    @Test
    public void create_ValidObject_SavedObject() {
        RestaurantTable table = new RestaurantTable();

        Room room = new Room();
        room.setRestaurantTables(new ArrayList<>());

        Mockito.when(roomServiceMock.getOne(1L)).thenReturn(room);

        restaurantTableService.create(table, 1L);

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(table);
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable();
        table.setName("name1");

        Mockito.when(restaurantTableRepositoryMock.findByName("name1")).thenReturn(Optional.of(new RestaurantTable()));

        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.create(table, 1L));
    }

    @Test
    public void update_ValidObject_ObjectIsUpdated() {
        RestaurantTable table = new RestaurantTable();
        table.setName("name1");
        table.setShape(TableShape.SQUARE);

        Mockito.when(restaurantTableRepositoryMock.findByName("name1")).thenReturn(Optional.empty());
        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(new RestaurantTable()));
        Mockito.when(restaurantTableRepositoryMock.save(Mockito.any(RestaurantTable.class))).thenAnswer(i -> i.getArguments()[0]);

        RestaurantTable restaurantTable = restaurantTableService.update(table, 1L, 1L);

        Assertions.assertEquals(restaurantTable.getShape(), table.getShape());
        Assertions.assertEquals(restaurantTable.getName(), table.getName());

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(Mockito.any(RestaurantTable.class));
    }

    @Test
    public void update_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable();
        table.setId(2L);
        table.setName("name1");

        Mockito.when(restaurantTableRepositoryMock.findByName("name1")).thenReturn(Optional.of(table));
        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(new RestaurantTable()));

        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.update(table, 1L, 1L));
    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        RestaurantTable table = new RestaurantTable();
        table.setName("name1");
        table.setShape(TableShape.SQUARE);

        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(new RestaurantTable()));

        restaurantTableService.delete(1L);

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(Mockito.any(RestaurantTable.class));
    }

    @Test
    public void getActiveOrderIdByTableId_InvalidState_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable();
        table.setState(TableState.FREE);

        Mockito.when(restaurantTableRepositoryMock.findByIdAndFetchOrder(1L)).thenReturn(Optional.of(table));

        Assertions.assertThrows(RestaurantTableStateNotValidException.class, () -> restaurantTableService.getActiveOrderIdByTableId(1L));
    }

    @Test
    public void getActiveOrderIdByTableId_ValidId_IdReturned() {
        RestaurantTable table = new RestaurantTable();
        table.setState(TableState.TAKEN);

        Order order = new Order();
        order.setId(1L);
        table.setActiveOrder(order);

        Mockito.when(restaurantTableRepositoryMock.findByIdAndFetchOrder(1L)).thenReturn(Optional.of(table));

        Long foundId = restaurantTableService.getActiveOrderIdByTableId(1L);

        Assertions.assertEquals(foundId, order.getId());
    }


}