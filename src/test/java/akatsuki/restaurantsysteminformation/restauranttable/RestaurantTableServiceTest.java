package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableStateNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RestaurantTableServiceTest {

    @InjectMocks
    RestaurantTableServiceImpl restaurantTableService;
    @Mock
    RestaurantTableRepository restaurantTableRepositoryMock;

    @Test
    @DisplayName("When valid id is passed, object is returned")
    void getOne_ValidId_ObjectIsReturned() {
        RestaurantTable table = new RestaurantTable();

        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(table));
        RestaurantTable foundTable = restaurantTableService.getOne(1L);

        Assertions.assertEquals(foundTable, table);
    }

    @Test
    @DisplayName("When invalid id is passed, exception occurs")
    void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(restaurantTableRepositoryMock.findById(8000L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOne(8000L));
    }

    @Test
    @DisplayName("When valid id is passed, object is returned")
    void getOneWithOrder_ValidId_ObjectIsReturned() {
        RestaurantTable table = new RestaurantTable();

        Mockito.when(restaurantTableRepositoryMock.findByIdAndFetchOrder(1L)).thenReturn(Optional.of(table));
        RestaurantTable foundTable = restaurantTableService.getOneWithOrder(1L);
        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOne(8000L));

        Assertions.assertEquals(foundTable, table);
    }

    @Test
    @DisplayName("When invalid id is passed, exception occurs")
    void getOneWithOrder_InvalidId_ExceptionThrown() {
        Mockito.when(restaurantTableRepositoryMock.findByIdAndFetchOrder(8000L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> restaurantTableService.getOneWithOrder(8000L));
    }

    @Test
    @DisplayName("When there are objects in the database, they are returned as list.")
    void getAll_ObjectsExist_ReturnedAsList() {
        Mockito.when(restaurantTableRepositoryMock.findAll()).thenReturn(List.of(new RestaurantTable(), new RestaurantTable()));

        List<RestaurantTable> list = restaurantTableService.getAll();

        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    @DisplayName("When valid dto is passed, new object is created.")
    public void create_ValidObject_SavedObject() {
        RestaurantTable table = new RestaurantTable();
        table.setName("name1");

        Mockito.when(restaurantTableRepositoryMock.findByName("name1")).thenReturn(Optional.empty());

        restaurantTableService.create(table);

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(table);
    }

    @Test
    @DisplayName("When invalid name is passed, exception occurs.")
    public void create_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable();
        table.setName("name1");

        Mockito.when(restaurantTableRepositoryMock.findByName("name1")).thenReturn(Optional.of(new RestaurantTable()));

        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.create(table));
    }

    @Test
    @DisplayName("When valid dto is passed, new object is updated.")
    public void update_ValidObject_ObjectIsUpdated() {
        RestaurantTable table = new RestaurantTable();
        table.setName("name1");
        table.setShape(TableShape.SQUARE);

        Mockito.when(restaurantTableRepositoryMock.findByName("name1")).thenReturn(Optional.empty());
        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(new RestaurantTable()));
        Mockito.when(restaurantTableRepositoryMock.save(Mockito.any(RestaurantTable.class))).thenAnswer(i -> i.getArguments()[0]);

        RestaurantTable restaurantTable = restaurantTableService.update(table, 1L);

        Assertions.assertEquals(restaurantTable.getShape(), table.getShape());
        Assertions.assertEquals(restaurantTable.getName(), table.getName());

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(Mockito.any(RestaurantTable.class));
    }

    @Test
    @DisplayName("When invalid name is passed, exception occurs.")
    public void update_InvalidName_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable();
        table.setId(2L);
        table.setName("name1");

        Mockito.when(restaurantTableRepositoryMock.findByName("name1")).thenReturn(Optional.of(table));
        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(new RestaurantTable()));

        Assertions.assertThrows(RestaurantTableExistsException.class, () -> restaurantTableService.update(table, 1L));
    }

    @Test
    @DisplayName("When valid id is passed, object is deleted.")
    public void delete_ValidId_ObjectRemoved() {
        RestaurantTable table = new RestaurantTable();
        table.setName("name1");
        table.setShape(TableShape.SQUARE);

        Mockito.when(restaurantTableRepositoryMock.findById(1L)).thenReturn(Optional.of(new RestaurantTable()));

        restaurantTableService.delete(1L);

        Mockito.verify(restaurantTableRepositoryMock, Mockito.times(1)).save(Mockito.any(RestaurantTable.class));
    }

    @Test
    @DisplayName("When table state is nov valid, exception occurs.")
    public void getActiveOrderIdByTableId_InvalidState_ExceptionThrown() {
        RestaurantTable table = new RestaurantTable();
        table.setState(TableState.FREE);

        Mockito.when(restaurantTableRepositoryMock.findByIdAndFetchOrder(1L)).thenReturn(Optional.of(table));

        Assertions.assertThrows(RestaurantTableStateNotValidException.class, () -> restaurantTableService.getActiveOrderIdByTableId(1L));
    }

    @Test
    @DisplayName("When valid table id is passed, order id is returned.")
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