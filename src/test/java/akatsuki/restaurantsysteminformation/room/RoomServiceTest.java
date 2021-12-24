package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.dto.RoomLayoutDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomTablesUpdateDTO;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletionFailedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomLayoutUpdateException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    RoomServiceImpl roomService;
    @Mock
    RoomRepository roomRepositoryMock;
    @Mock
    RestaurantTableService restaurantTableServiceMock;

    @Test
    void getOne_ValidId_ObjectIsReturned() {
        Room room = new Room();

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));
        Room foundRoom = roomService.getOne(1L);

        Assertions.assertEquals(foundRoom, room);
    }

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(roomRepositoryMock.findById(8000L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RoomNotFoundException.class, () -> roomService.getOne(8000L));
    }

    @Test
    void getAll_ObjectsExist_ReturnedAsList() {
        Mockito.when(roomRepositoryMock.findAll()).thenReturn(List.of(new Room(), new Room()));

        List<Room> list = roomService.getAll();

        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    public void create_ValidObject_SavedObject() {
        Room room = new Room();
        room.setName("name1");

        Mockito.when(roomRepositoryMock.findByName("name1")).thenReturn(Optional.empty());

        roomService.create(room);

        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(room);
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        Room room = new Room();
        room.setName("name1");

        Mockito.when(roomRepositoryMock.findByName("name1")).thenReturn(Optional.of(new Room()));

        Assertions.assertThrows(RoomExistsException.class, () -> roomService.create(room));
    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        Room room = new Room();
        room.setName("name1");

        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setId(1L);

        room.setRestaurantTables(Collections.singletonList(restaurantTable));

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));

        roomService.delete(1L);

        Mockito.verify(restaurantTableServiceMock, Mockito.times(1)).delete(1L);
        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(Mockito.any(Room.class));
    }

    @Test
    public void delete_OrderExist_ExceptionThrown() {
        Room room = new Room();
        room.setName("name1");

        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setId(1L);
        restaurantTable.setActiveOrder(new Order());

        room.setRestaurantTables(Collections.singletonList(restaurantTable));

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));

        Assertions.assertThrows(RoomDeletionFailedException.class, () -> roomService.delete(1L));
    }

    @Test
    public void updateName_ValidName_ObjectUpdated() {
        Room room = new Room();

        Mockito.when(roomRepositoryMock.findByName("Room number 1")).thenReturn(Optional.empty());
        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));

        roomService.updateName("Room number 1", 1L);

        Assertions.assertNotNull(room);
        Assertions.assertEquals("Room number 1", room.getName());

        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(Mockito.any(Room.class));
    }

    @Test
    public void updateName_NameAlreadyExist_ExceptionThrown() {
        Room room = new Room();
        room.setId(2L);

        Mockito.when(roomRepositoryMock.findByName("Room number 1")).thenReturn(Optional.of(room));

        Assertions.assertThrows(RoomExistsException.class,
                () -> roomService.updateName("Room number 1", 1L));
    }

    @Test
    public void updateLayout_ValidObject_ObjectIsUpdated() {
        RestaurantTable table1 = new RestaurantTable();
        table1.setRow(0);
        table1.setColumn(0);

        RestaurantTable table2 = new RestaurantTable();
        table2.setRow(1);
        table2.setColumn(1);

        Room room = new Room();
        room.setColumns(4);
        room.setRows(4);
        room.setRestaurantTables(List.of(table1, table2));

        RoomLayoutDTO dto = new RoomLayoutDTO(7, 7);

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));

        roomService.updateLayout(dto, 1L);

        Assertions.assertEquals(7, room.getRows());
        Assertions.assertEquals(7, room.getColumns());
        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(Mockito.any(Room.class));
    }

    @Test
    public void updateLayout_TableOutOfNewLayout_ExceptionThrown() {
        RestaurantTable table1 = new RestaurantTable();
        table1.setRow(0);
        table1.setColumn(0);

        RestaurantTable table2 = new RestaurantTable();
        table2.setRow(2);
        table2.setColumn(2);

        Room room = new Room();
        room.setRows(4);
        room.setColumns(4);
        room.setRestaurantTables(List.of(table1, table2));

        RoomLayoutDTO dto = new RoomLayoutDTO(1, 1);

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));

        Assertions.assertThrows(RoomLayoutUpdateException.class,
                () -> roomService.updateLayout(dto, 1L));
    }

    @Test
    public void updateTables_ValidTables_ObjectUpdated() {
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(1L);
        table1.setName("T1");

        RestaurantTable table2 = new RestaurantTable();
        table2.setId(2L);
        table2.setName("T2");

        RestaurantTable table3 = new RestaurantTable();
        table3.setId(3L);
        table3.setName("T3");

        Room room = new Room();
        room.setRestaurantTables(List.of(table1, table2, table3));

        RestaurantTableDTO restaurantTableDTO = new RestaurantTableDTO();
        restaurantTableDTO.setId(0L);
        restaurantTableDTO.setName("T1");

        RestaurantTableDTO restaurantTableDTO2 = new RestaurantTableDTO();
        restaurantTableDTO2.setId(0L);
        restaurantTableDTO2.setName("T5");

        RestaurantTableDTO restaurantTableDTO3 = new RestaurantTableDTO();
        restaurantTableDTO3.setId(2L);
        restaurantTableDTO3.setName("T8");

        RoomTablesUpdateDTO dto = new RoomTablesUpdateDTO(List.of(restaurantTableDTO, restaurantTableDTO2, restaurantTableDTO3));

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));
        Mockito.when(restaurantTableServiceMock.getOne(1L)).thenReturn(table1);
        Mockito.when(restaurantTableServiceMock.getOne(2L)).thenReturn(table2);
        Mockito.when(restaurantTableServiceMock.create(Mockito.any(RestaurantTable.class), Mockito.any(Long.class))).thenReturn(new RestaurantTable());
        Mockito.when(restaurantTableServiceMock.update(Mockito.any(RestaurantTable.class), Mockito.any(Long.class), Mockito.any(Long.class))).thenReturn(new RestaurantTable());

        roomService.updateTables(dto, 1L);

        Assertions.assertEquals(3, room.getRestaurantTables().size());

        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(Mockito.any(Room.class));
        Mockito.verify(restaurantTableServiceMock, Mockito.times(1)).delete(3);
    }

    @Test
    public void updateTables_TableNotInRoom_ExceptionThrown() {
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(0L);
        table1.setName("T1");

        Room room = new Room();
        room.setRestaurantTables(List.of(table1));

        RestaurantTableDTO restaurantTableDTO = new RestaurantTableDTO();
        restaurantTableDTO.setId(0L);
        restaurantTableDTO.setName("T1");

        RoomTablesUpdateDTO dto = new RoomTablesUpdateDTO(List.of(restaurantTableDTO));

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));
        Mockito.when(restaurantTableServiceMock.getOne(0L)).thenReturn(new RestaurantTable());

        Assertions.assertThrows(RestaurantTableNotAvailableException.class,
                () -> roomService.updateTables(dto, 1L));
    }

}