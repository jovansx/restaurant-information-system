package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletionFailedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
    @DisplayName("When valid id is passed, object is returned")
    void getOne_ValidId_ObjectIsReturned() {
        Room room = new Room();

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room));
        Room foundRoom = roomService.getOne(1L);

        Assertions.assertEquals(foundRoom, room);
    }

    @Test
    @DisplayName("When invalid id is passed, exception occurs")
    void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(roomRepositoryMock.findById(8000L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RoomNotFoundException.class, () -> roomService.getOne(8000L));
    }

    @Test
    @DisplayName("When there are objects in the database, they are returned as list.")
    void getAll_ObjectsExist_ReturnedAsList() {
        Mockito.when(roomRepositoryMock.findAll()).thenReturn(List.of(new Room(), new Room()));

        List<Room> list = roomService.getAll();

        Assertions.assertEquals(list.size(), 2);
    }

    @Test
    @DisplayName("When valid object is passed, new object is created.")
    public void create_ValidObject_SavedObject() {
        Room room = new Room();
        room.setName("name1");

        Mockito.when(roomRepositoryMock.findByName("name1")).thenReturn(Optional.empty());

        roomService.create(room);

        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(room);
    }

    @Test
    @DisplayName("When invalid name is passed, exception occurs.")
    public void create_InvalidName_ExceptionThrown() {
        Room room = new Room();
        room.setName("name1");

        Mockito.when(roomRepositoryMock.findByName("name1")).thenReturn(Optional.of(new Room()));

        Assertions.assertThrows(RoomExistsException.class, () -> roomService.create(room));
    }

    @Test
    @DisplayName("When valid dto is passed, new object is updated.")
    public void update_ValidObject_ObjectIsUpdated() {
        Room room = new Room();
        room.setRestaurantTables(Collections.singletonList(new RestaurantTable()));
        room.setName("name1");

        Room room2 = new Room();
        room2.setName("name2");

        Mockito.when(roomRepositoryMock.findById(1L)).thenReturn(Optional.of(room2));

        roomService.update(room, 1L);

        Assertions.assertEquals(room2.getRestaurantTables().size(), 1);
        Assertions.assertEquals(room2.getName(), room.getName());

        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(Mockito.any(Room.class));
    }

    @Test
    @DisplayName("When valid id is passed, object is deleted.")
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
    @DisplayName("When order exist, room cannot be deleted and exception occur.")
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
    @DisplayName("When room name already exist, exception occur.")
    public void updateByRoomDTO_RoomNameExist_ExceptionThrown() {
        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "name1");

        Room room2 = new Room();
        room2.setId(2L);

        Mockito.when(roomRepositoryMock.findByName("name1")).thenReturn(Optional.of(room2));

        Assertions.assertThrows(RoomExistsException.class, () -> roomService.updateByRoomDTO(roomUpdateDTO, 1L));
    }

    @Test
    @DisplayName("When table is not in that room, exception occur.")
    public void updateByRoomDTO_TableNotInRoom_ExceptionThrown() {
        List<RestaurantTableCreateDTO> listCreate = new ArrayList<>();
        listCreate.add(new RestaurantTableCreateDTO("table1", TableState.FREE.name(), TableShape.SQUARE.name()));

        List<RestaurantTableDTO> listUpdate = new ArrayList<>();
        RestaurantTable table = new RestaurantTable("table2", TableState.FREE, TableShape.SQUARE, false, null);
        table.setId(2L);
        listUpdate.add(new RestaurantTableDTO(table));

        List<Long> listDelete = new ArrayList<>();
        listDelete.add(3L);

        Room returnedRoom = new Room();
        returnedRoom.setRestaurantTables(new ArrayList<>());

        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(listCreate, listUpdate, listDelete, "name1");

        Mockito.when(roomRepositoryMock.findByName("name1")).thenReturn(Optional.empty());
        Mockito.when(roomRepositoryMock.findById(Mockito.any())).thenReturn(Optional.of(returnedRoom));
        Mockito.when(restaurantTableServiceMock.getOne(Mockito.any(Long.class))).thenReturn(new RestaurantTable());

        Assertions.assertThrows(RestaurantTableNotAvailableException.class, () -> roomService.updateByRoomDTO(roomUpdateDTO, 1L));

    }

    @Test
    @DisplayName("When valid dto is passed, object is updated.")
    public void updateByRoomDTO_ValidDto_ObjectUpdated() {
        List<RestaurantTableCreateDTO> listCreate = new ArrayList<>();
        listCreate.add(new RestaurantTableCreateDTO("table1", TableState.FREE.name(), TableShape.SQUARE.name()));

        List<RestaurantTableDTO> listUpdate = new ArrayList<>();
        RestaurantTable table = new RestaurantTable("table2", TableState.FREE, TableShape.SQUARE, false, null);
        table.setId(2L);
        listUpdate.add(new RestaurantTableDTO(table));

        List<Long> listDelete = new ArrayList<>();
        listDelete.add(3L);

        RestaurantTable restaurantTable = new RestaurantTable();

        Room returnedRoom = new Room();
        returnedRoom.setRestaurantTables(Collections.singletonList(restaurantTable));

        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(listCreate, listUpdate, listDelete, "name1");

        Mockito.when(roomRepositoryMock.findByName("name1")).thenReturn(Optional.empty());
        Mockito.when(roomRepositoryMock.findById(Mockito.any())).thenReturn(Optional.of(returnedRoom)).thenReturn(Optional.of(returnedRoom)).thenReturn(Optional.of(returnedRoom));
        Mockito.when(restaurantTableServiceMock.getOne(Mockito.any(Long.class))).thenReturn(restaurantTable);

        roomService.updateByRoomDTO(roomUpdateDTO, 1L);

        Mockito.verify(restaurantTableServiceMock, Mockito.times(1)).create(Mockito.any(RestaurantTable.class));
        Mockito.verify(restaurantTableServiceMock, Mockito.times(1)).update(Mockito.any(RestaurantTable.class), Mockito.any(long.class));
        Mockito.verify(restaurantTableServiceMock, Mockito.times(1)).delete(Mockito.any(Long.class));
        Mockito.verify(roomRepositoryMock, Mockito.times(1)).save(Mockito.any(Room.class));
    }

}