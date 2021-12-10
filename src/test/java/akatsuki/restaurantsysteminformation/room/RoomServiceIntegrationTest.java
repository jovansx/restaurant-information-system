package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomServiceIntegrationTest {

    @Autowired
    RoomServiceImpl roomService;

    @Test
    void getOne_ValidId_ObjectIsReturned() {
        Room foundRoom = roomService.getOne(1L);
        Assertions.assertNotNull(foundRoom);
    }

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(RoomNotFoundException.class, () -> roomService.getOne(8000L));
    }

    @Test
    void getAll_Valid_ReturnedList() {
        List<Room> rooms = roomService.getAll();
        Assertions.assertNotNull(rooms);
        Assertions.assertEquals(3, rooms.size());
    }

    @Test
    public void create_ValidObject_SavedObject() {
        Room room = new Room();
        room.setName("name1");
        Room createdRoom = roomService.create(room);
        Assertions.assertNotNull(createdRoom);
        Assertions.assertEquals("name1", createdRoom.getName());
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        Room room = new Room();
        room.setName("Room 1");
        Assertions.assertThrows(RoomExistsException.class, () -> roomService.create(room));
    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        Room room = roomService.delete(3L);
        Assertions.assertNotNull(room);
    }

    @Test
    public void delete_OrderExist_ExceptionThrown() {
        Assertions.assertThrows(RoomDeletionFailedException.class, () -> roomService.delete(1L));
    }

    @Test
    public void updateName_ValidName_ObjectUpdated() {
        Room room = roomService.updateName("Room number 1", 1);
        Assertions.assertNotNull(room);
        Assertions.assertEquals("Room number 1", room.getName());
    }

    @Test
    public void updateName_NameAlreadyExist_ExceptionThrown() {
        Assertions.assertThrows(RoomExistsException.class, () -> roomService.updateName("Room 2", 1));
    }

    @Test
    public void updateLayout_NewValidLayout_ObjectUpdated() {
        Room room = roomService.updateLayout(new RoomLayoutDTO(5, 5), 1);
        Assertions.assertNotNull(room);
        Assertions.assertEquals(5, room.getColumns());
        Assertions.assertEquals(5, room.getRows());
    }

    @Test
    public void updateLayout_TableOutOfNewLayout_ExceptionThrown() {
        Assertions.assertThrows(RoomLayoutUpdateException.class, () -> roomService.updateLayout(new RoomLayoutDTO(1, 1), 1));
    }

    @Test
    public void updateTables_ValidTables_ObjectUpdated() {
        RestaurantTableDTO restaurantTableDTO1 = new RestaurantTableDTO();
        restaurantTableDTO1.setId(1);
        restaurantTableDTO1.setName("Table 1");
        restaurantTableDTO1.setShape(TableShape.CIRCLE);
        restaurantTableDTO1.setRow(1);

        RestaurantTableDTO restaurantTableDTO2 = new RestaurantTableDTO();
        restaurantTableDTO2.setId(0);
        restaurantTableDTO2.setName("Table 3");
        restaurantTableDTO2.setShape(TableShape.CIRCLE);
        restaurantTableDTO2.setState(TableState.FREE);
        restaurantTableDTO2.setColumn(0);

        RestaurantTableDTO restaurantTableDTO3 = new RestaurantTableDTO();
        restaurantTableDTO3.setId(0);
        restaurantTableDTO3.setName("T2");
        restaurantTableDTO3.setShape(TableShape.CIRCLE);
        restaurantTableDTO3.setState(TableState.FREE);
        restaurantTableDTO3.setColumn(0);

        RoomTablesUpdateDTO roomUpdateDTO = new RoomTablesUpdateDTO(Arrays.asList(restaurantTableDTO1, restaurantTableDTO2, restaurantTableDTO3));

        Room room = roomService.updateTables(roomUpdateDTO, 1L);
        List<RestaurantTable> tables = room.getRestaurantTables();

        RestaurantTable table = tables.stream().filter(t -> t.getName().equals(restaurantTableDTO1.getName())).collect(Collectors.toList()).get(0);
        RestaurantTable table2 = tables.stream().filter(t -> t.getName().equals(restaurantTableDTO2.getName())).collect(Collectors.toList()).get(0);
        RestaurantTable table3 = tables.stream().filter(t -> t.getName().equals(restaurantTableDTO3.getName())).collect(Collectors.toList()).get(0);

        Assertions.assertNotNull(room);
        Assertions.assertEquals(1, table.getRow());
        Assertions.assertEquals(TableShape.CIRCLE, table.getShape());

        Assertions.assertNotEquals(0, table2.getId());
        Assertions.assertEquals(TableShape.CIRCLE, table2.getShape());
        Assertions.assertEquals(TableState.FREE, table2.getState());
        Assertions.assertEquals(0, table2.getColumn());

        Assertions.assertNotEquals(0, table3.getId());
        Assertions.assertEquals(TableShape.CIRCLE, table3.getShape());
        Assertions.assertEquals(TableState.FREE, table3.getState());
        Assertions.assertEquals(0, table3.getColumn());
    }

    @Test
    public void updateTables_TableNotInRoom_ExceptionThrown() {
        RestaurantTableDTO table = new RestaurantTableDTO();
        table.setId(3L);

        RoomTablesUpdateDTO roomUpdateDTO = new RoomTablesUpdateDTO(Collections.singletonList(table));
        Assertions.assertThrows(RestaurantTableNotAvailableException.class, () -> roomService.updateTables(roomUpdateDTO, 1L));
    }

}