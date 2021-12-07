package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomCreateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomWithTablesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/room";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    RoomService roomService;

    @Test
    void getOne_ValidId_ObjectIsReturned() {
        ResponseEntity<RoomWithTablesDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/1", RoomWithTablesDTO.class);
        RoomWithTablesDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("Room 1", dto.getName());
    }

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        ResponseEntity<RoomWithTablesDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/8000", RoomWithTablesDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getAll_Valid_ObjectsReturned() {
        ResponseEntity<RoomWithTablesDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX, RoomWithTablesDTO[].class);
        List<RoomWithTablesDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void create_ValidObject_SavedObject() {
        int size = roomService.getAll().size();
        ResponseEntity<String> res = restTemplate.postForEntity(URL_PREFIX, new RoomCreateDTO("Room 3"), String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<Room> rooms = roomService.getAll();
        Assertions.assertEquals(size + 1, rooms.size());
        Assertions.assertEquals("Room 3", rooms.get(rooms.size() - 1).getName());

        roomService.delete(Long.parseLong(res.getBody()));
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PREFIX, new RoomCreateDTO("Room 1"), String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void update_ValidObject_ObjectIsUpdated() {

        List<RestaurantTableCreateDTO> newTables = new ArrayList<>();
        newTables.add(new RestaurantTableCreateDTO("T3", TableState.FREE, TableShape.SQUARE, 0, 0));
        List<RestaurantTableDTO> updateTables = new ArrayList<>();
        List<Long> deleteTables = new ArrayList<>();

        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(newTables, updateTables, deleteTables, "Room number 1");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                        new HttpEntity<>(roomUpdateDTO),
                        Void.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Room room = roomService.getOne(1L);
        Assertions.assertEquals("Room number 1", room.getName());
        RestaurantTable table = null;
        for(RestaurantTable t : room.getRestaurantTables()) {
            if(t.getName().equals("T3")) {
                table = t;
            }
        }
        Assertions.assertNotNull(table);

        deleteTables.add(table.getId());
        roomUpdateDTO = new RoomUpdateDTO(new ArrayList<>(), new ArrayList<>(), deleteTables, "Room 1");
        roomService.updateByRoomDTO(roomUpdateDTO, 1L);
    }

    @Test
    public void update_TableNotInRoom_ExceptionThrown() {
        List<RestaurantTableCreateDTO> newTables = new ArrayList<>();
        List<RestaurantTableDTO> updateTables = new ArrayList<>();
        List<Long> deleteTables = new ArrayList<>();
        deleteTables.add(4L);

        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(newTables, updateTables, deleteTables, "Room number 1");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                        new HttpEntity<>(roomUpdateDTO),
                        Void.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        Room room = roomService.create(new Room("room 4", false, null, 0, 0));
        int size = roomService.getAll().size();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/" + room.getId(),
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size - 1, roomService.getAll().size());
    }

    @Test
    public void delete_OrderExist_ExceptionThrown() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/1",
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void updateByRoomDTO_RoomNameExist_ExceptionThrown() {
        List<RestaurantTableCreateDTO> newTables = new ArrayList<>();
        List<RestaurantTableDTO> updateTables = new ArrayList<>();
        List<Long> deleteTables = new ArrayList<>();

        RoomUpdateDTO roomUpdateDTO = new RoomUpdateDTO(newTables, updateTables, deleteTables, "Room 1");

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/2", HttpMethod.PUT,
                        new HttpEntity<>(roomUpdateDTO),
                        Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

}