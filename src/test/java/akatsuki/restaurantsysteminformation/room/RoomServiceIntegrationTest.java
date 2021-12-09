package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletionFailedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
    public void create_ValidObject_SavedObject() {
        Room room = new Room();
        room.setName("name1");
        Room createdRoom = roomService.create(room);
        Assertions.assertNotNull(createdRoom);
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        Room room = new Room();
        room.setName("Room 1");
        Assertions.assertThrows(RoomExistsException.class, () -> roomService.create(room));
    }
//
//    @Test
//    public void update_ValidObject_ObjectIsUpdated() {
//        List<RestaurantTableCreateDTO> newTables = new ArrayList<>();
//        newTables.add(new RestaurantTableCreateDTO("T3", TableState.FREE, TableShape.SQUARE, 0, 0));
//        List<RestaurantTableDTO> updateTables = new ArrayList<>();
//        List<Long> deleteTables = new ArrayList<>();
//
//        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(newTables, updateTables, deleteTables, "Room number 1");
//
//        Room updatedRoom = roomService.updateByRoomDTO(roomUpdateDTO, 1L);
//        Assertions.assertNotNull(updatedRoom);
//    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        Room room = roomService.delete(2L);
        Assertions.assertNotNull(room);
    }

    @Test
    public void delete_OrderExist_ExceptionThrown() {
        Assertions.assertThrows(RoomDeletionFailedException.class, () -> roomService.delete(1L));
    }

//    @Test
//    public void updateByRoomDTO_RoomNameExist_ExceptionThrown() {
//        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "Room 2");
//        Assertions.assertThrows(RoomExistsException.class, () -> roomService.updateByRoomDTO(roomUpdateDTO, 1L));
//    }

//    @Test
//    public void updateByRoomDTO_TableNotInRoom_ExceptionThrown() {
//        List<RestaurantTableCreateDTO> newTables = new ArrayList<>();
//        List<RestaurantTableDTO> updateTables = new ArrayList<>();
//        updateTables.add(new RestaurantTableDTO("T5", TableState.FREE, TableShape.SQUARE, 1L, 0, 0));
//        List<Long> deleteTables = new ArrayList<>();
//
//        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(newTables, updateTables, deleteTables, "Room number 2");
//        Assertions.assertThrows(RestaurantTableNotAvailableException.class, () -> roomService.updateByRoomDTO(roomUpdateDTO, 2L));
//    }

}