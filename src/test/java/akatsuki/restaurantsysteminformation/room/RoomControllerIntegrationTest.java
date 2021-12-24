package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.dto.LoginDTO;
import akatsuki.restaurantsysteminformation.dto.TokenDTO;
import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomCreateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomLayoutDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomTablesUpdateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomWithTablesDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/room";
    @Autowired
    RoomService roomService;
    @Autowired
    RestaurantTableService restaurantTableService;
    private HttpHeaders headers = null;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void login() {
        if (headers == null) {
            ResponseEntity<TokenDTO> responseEntity =
                    restTemplate.postForEntity("/api/authenticate",
                            new LoginDTO("michaeldouglas", "michaeldouglas"),
                            TokenDTO.class);
            String accessToken = Objects.requireNonNull(responseEntity.getBody()).getToken();
            this.headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
        }
    }

    @Test
    public void getAll_Valid_ObjectsReturned() {
        ResponseEntity<RoomWithTablesDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX, RoomWithTablesDTO[].class);
        List<RoomWithTablesDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void create_ValidObject_SavedObject() {
        int size = roomService.getAll().size();

        ResponseEntity<String> res =
                restTemplate.exchange(URL_PREFIX, HttpMethod.POST, new HttpEntity<>(new RoomCreateDTO("Room 3"), headers), String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<Room> rooms = roomService.getAll();
        Room addedRoom = rooms.get(rooms.size() - 1);

        Assertions.assertEquals(size + 1, rooms.size());
        Assertions.assertEquals("Room 3", addedRoom.getName());
        Assertions.assertEquals(2, addedRoom.getColumns());
        Assertions.assertEquals(2, addedRoom.getRows());

        roomService.delete(Long.parseLong(res.getBody()));
    }

    @Test
    public void create_InvalidName_ExceptionThrown() {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PREFIX, new HttpEntity<>(new RoomCreateDTO("Room 1"), headers), String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void updateRoomName_ValidObject_ObjectIsUpdated() {
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1/name", HttpMethod.PUT,
                        new HttpEntity<>("Room 5", this.headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Room room = roomService.getOne(1L);
        Assertions.assertEquals("Room 5", room.getName());

        roomService.updateName("Room 1", 1);
    }

    @Test
    public void updateRoomName_NameAlreadyExist_ExceptionThrown() {
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1/name", HttpMethod.PUT,
                        new HttpEntity<>("Room 2", this.headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void updateRoomLayout_ValidObject_ObjectIsUpdated() {
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1/layout", HttpMethod.PUT,
                        new HttpEntity<>(new RoomLayoutDTO(5, 5), this.headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Room room = roomService.getOne(1L);
        Assertions.assertEquals(5, room.getRows());
        Assertions.assertEquals(5, room.getColumns());

        roomService.updateLayout(new RoomLayoutDTO(4, 4), 1);
    }

    @Test
    public void updateRoomLayout_TableExistOutOfLayout_ExceptionThrown() {
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1/layout", HttpMethod.PUT,
                        new HttpEntity<>(new RoomLayoutDTO(1, 1), this.headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        RestaurantTable table = restaurantTableService.create(new RestaurantTable(
                "Table 1", TableState.FREE, TableShape.SQUARE, false, null, 0, 1), 1);
        Room room = roomService.create(new Room("room 4", false, Collections.singletonList(table), 0, 0));

        int tablesSize = restaurantTableService.getAll().size();
        int roomsSize = roomService.getAll().size();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/" + room.getId(),
                HttpMethod.DELETE, new HttpEntity<>(this.headers), Void.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(tablesSize - 1, restaurantTableService.getAll().size());
        Assertions.assertEquals(roomsSize - 1, roomService.getAll().size());
    }

    @Test
    public void delete_OrderExist_ExceptionThrown() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/1",
                HttpMethod.DELETE, new HttpEntity<>(this.headers), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void delete_RoomDoesNotExist_ExceptionThrown() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/4000",
                HttpMethod.DELETE, new HttpEntity<>(this.headers), Void.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateRoomTables_ValidTables_ObjectUpdated() {
        RestaurantTable table1 = restaurantTableService.create(new RestaurantTable(
                "Table 1", TableState.FREE, TableShape.SQUARE, false, null, 0, 1), 1);
        RestaurantTable table2 = restaurantTableService.create(new RestaurantTable(
                "Table 2", TableState.FREE, TableShape.SQUARE, false, null, 1, 1), 1);
        RestaurantTable table3 = restaurantTableService.create(new RestaurantTable(
                "Table 4", TableState.FREE, TableShape.SQUARE, false, null, 0, 1), 1);
        Room room = roomService.create(new Room("room 4", false, Arrays.asList(table1, table2, table3), 4, 4));

        int size = restaurantTableService.getAll().size();

        RestaurantTableDTO restaurantTableDTO1 = new RestaurantTableDTO(table1);
        restaurantTableDTO1.setShape(TableShape.CIRCLE);
        restaurantTableDTO1.setRow(1);

        RestaurantTableDTO restaurantTableDTO2 = new RestaurantTableDTO(table2);
        restaurantTableDTO2.setId(0);
        restaurantTableDTO2.setName("Table 3");
        restaurantTableDTO2.setColumn(0);

        RestaurantTableDTO restaurantTableDTO3 = new RestaurantTableDTO(table2);
        restaurantTableDTO3.setId(0);
        restaurantTableDTO3.setName("Table 4");
        restaurantTableDTO3.setColumn(0);

        RoomTablesUpdateDTO roomUpdateDTO = new RoomTablesUpdateDTO(Arrays.asList(restaurantTableDTO1, restaurantTableDTO2, restaurantTableDTO3));

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/" + room.getId() + "/tables", HttpMethod.PUT,
                        new HttpEntity<>(roomUpdateDTO, this.headers),
                        Void.class);

        List<RestaurantTable> list = restaurantTableService.getAll();
        RestaurantTable updatedTable1 = restaurantTableService.getOne(table1.getId());
        RestaurantTable updatedTable2 = restaurantTableService.getOne(table3.getId());
        RestaurantTable addedTable2 = list.stream().filter(table -> table.getName().equals(restaurantTableDTO2.getName())).collect(Collectors.toList()).get(0);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(1, updatedTable1.getRow());
        Assertions.assertEquals(TableShape.CIRCLE, updatedTable1.getShape());
        Assertions.assertEquals(0, updatedTable2.getColumn());
        Assertions.assertEquals(0, addedTable2.getColumn());
        Assertions.assertNotEquals(0, addedTable2.getId());
        Assertions.assertEquals(size, restaurantTableService.getAll().size());

        roomService.delete(room.getId());
    }

    @Test
    public void updateRoomTables_TableNotInRoom_ExceptionThrown() {
        RestaurantTable table1 = restaurantTableService.create(new RestaurantTable(
                "Table 1", TableState.FREE, TableShape.SQUARE, false, null, 0, 1), 1);
        Room room = roomService.create(new Room("room 4", false, List.of(table1), 4, 4));

        RestaurantTableDTO restaurantTableDTO1 = new RestaurantTableDTO(table1);
        restaurantTableDTO1.setId(1);
        restaurantTableDTO1.setShape(TableShape.CIRCLE);
        restaurantTableDTO1.setRow(1);

        RoomTablesUpdateDTO roomUpdateDTO = new RoomTablesUpdateDTO(List.of(restaurantTableDTO1));

        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/" + room.getId() + "/tables", HttpMethod.PUT,
                        new HttpEntity<>(roomUpdateDTO, this.headers),
                        Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());

        roomService.delete(room.getId());
    }

}