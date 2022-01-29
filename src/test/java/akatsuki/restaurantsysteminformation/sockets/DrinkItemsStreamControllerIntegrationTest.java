package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsActionRequestDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsUpdateDTO;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.sockets.dto.SocketResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DrinkItemsStreamControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private DrinkItemsService drinkItemsService;

    @Autowired
    private OrderService orderService;

    private BlockingQueue<SocketResponseDTO> blockingQueue;
    private StompSession session;

    @BeforeEach
    public void setup() throws Exception {
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        blockingQueue = new ArrayBlockingQueue<>(1);

        session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);
        session.subscribe("/topic/drink-items", new MyStompFrameHandler(blockingQueue));
    }

    @Test
    public void create_Valid_SavedObject() throws Exception {

        List<DrinkItemUpdateDTO> drinkItemList = new ArrayList<>();
        drinkItemList.add(new DrinkItemUpdateDTO(2, 1L, 1L, 3));
        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, drinkItemList, "Notes", null);

        session.send("/app/drink-items/create", drinkItemsCreateDTO);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());
        assertEquals("Dish item is successfully created!", returnDTO.getMessage());
        drinkItemsService.deleteById(returnDTO.getId());
    }

    @Test
    public void create_InvalidItemTypeDto_ExceptionThrown() throws Exception {
        List<DrinkItemUpdateDTO> drinkItemList = new ArrayList<>();
        drinkItemList.add(new DrinkItemUpdateDTO(2, 8000L, 1L, 3));
        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(1, drinkItemList, "Notes", null);

        session.send("/app/drink-items/create", drinkItemsCreateDTO);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Item with the id 8000 is not found in the database.", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void create_InvalidOrderId_ExceptionThrown() throws Exception {

        List<DrinkItemUpdateDTO> drinkItemList = new ArrayList<>();
        drinkItemList.add(new DrinkItemUpdateDTO(2, 1L, 1L, 3));
        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(8000, drinkItemList, "Notes", null);

        session.send("/app/drink-items/create", drinkItemsCreateDTO);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Order with the id 8000 is not found in the database.", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void update_Valid_UpdatedObject() throws Exception {

        List<DrinkItemUpdateDTO> drinkItems = new ArrayList<>();
        drinkItems.add(new DrinkItemUpdateDTO(14, 1L, 3L, 1));
        DrinkItemsUpdateDTO dto = new DrinkItemsUpdateDTO(1, drinkItems, "Notes");

        session.send("/app/drink-items/update/6", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with 6 are successfully updated!", returnDTO.getMessage());

        drinkItems = new ArrayList<>();
        drinkItems.add(new DrinkItemUpdateDTO(1, 1L, 3L, 1));
        dto = new DrinkItemsUpdateDTO(1, drinkItems, "Notes");
        drinkItemsService.update(dto, 6L);

        Order order = orderService.getOneWithAll(1L);
        order.setTotalPrice(8);
        orderService.save(order);
    }

    @Test
    public void update_InvalidOrderId_ExceptionThrown() throws InterruptedException {
        List<DrinkItemUpdateDTO> drinkItems = new ArrayList<>();
        drinkItems.add(new DrinkItemUpdateDTO(14, 1L, 3L, 1));
        DrinkItemsUpdateDTO dto = new DrinkItemsUpdateDTO(8000, drinkItems, "Notes");

        session.send("/app/drink-items/update/5", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Order with the id 8000 is not found in the database.", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void update_InvalidDrinkItemsState_ExceptionThrown() throws Exception {
        List<DrinkItemUpdateDTO> drinkItems = new ArrayList<>();
        drinkItems.add(new DrinkItemUpdateDTO(14, 1L, 3L, 1));
        DrinkItemsUpdateDTO dto = new DrinkItemsUpdateDTO(1, drinkItems, "Notes");
        session.send("/app/drink-items/update/8", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Cannot change drink items list, because its state is ready. Allowed state to change is 'on_hold'.", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void update_InvalidItemId_ExceptionThrown() throws Exception {
        List<DrinkItemUpdateDTO> drinkItems = new ArrayList<>();
        drinkItems.add(new DrinkItemUpdateDTO(14, 1L, 9L, 1));
        DrinkItemsUpdateDTO dto = new DrinkItemsUpdateDTO(1, drinkItems, "Notes");

        session.send("/app/drink-items/update/6", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink item with the id 9 not found in the database.", returnDTO.getMessage());
    }

    @Test
    public void changeStateOfDrinkItems_OnHoldToPreparation_SavedObject() throws Exception {

        DrinkItemsActionRequestDTO dto = new DrinkItemsActionRequestDTO(2L, 7L);

        session.send("/app/drink-items/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());

        DrinkItems drinkItems = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(7L);
        drinkItems.setState(ItemState.ON_HOLD);
        drinkItems.setBartender(null);
        drinkItemsService.save(drinkItems);
    }

    @Test
    public void changeStateOfDrinkItems_PreparationToReady_SavedObject() throws Exception {

        DrinkItemsActionRequestDTO dto = new DrinkItemsActionRequestDTO(2L, 5L);

        session.send("/app/drink-items/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());

        DrinkItems drinkItems = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(5L);
        drinkItems.setState(ItemState.PREPARATION);
        drinkItemsService.save(drinkItems);
    }

    @Test
    public void changeStateOfDrinkItems_ReadyToDelivered_SavedObject() throws Exception {

        DrinkItemsActionRequestDTO dto = new DrinkItemsActionRequestDTO(1L, 8L);

        session.send("/app/drink-items/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());

        DrinkItems drinkItems = drinkItemsService.findOneActiveAndFetchBartenderAndItemsAndStateIsNotNew(8L);
        drinkItems.setState(ItemState.READY);
        drinkItemsService.save(drinkItems);
    }

    @Test
    public void changeStateOfDrinkItems_DrinkItemsId_ExceptionThrown() throws Exception {

        DrinkItemsActionRequestDTO dto = new DrinkItemsActionRequestDTO(1L, 1L);

        session.send("/app/drink-items/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with the id 1 are not found in the database.", returnDTO.getMessage());
    }

    @Test
    public void changeStateOfDrinkItems_WrongUserType_ExceptionThrown() throws Exception {

        DrinkItemsActionRequestDTO dto = new DrinkItemsActionRequestDTO(2L, 8L);

        session.send("/app/drink-items/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("User with the id 2 is not a waiter.", returnDTO.getMessage());
    }

    @Test
    public void delete_Valid_DeletedObject() throws Exception {

        List<DrinkItemUpdateDTO> drinkItemList = new ArrayList<>();
        drinkItemList.add(new DrinkItemUpdateDTO(2, 1L, 1L, 3));
        DrinkItemsCreateDTO drinkItemsCreateDTO = new DrinkItemsCreateDTO(7, drinkItemList, "Notes", null);

        session.send("/app/drink-items/create", drinkItemsCreateDTO);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        long id = returnDTO.getId();
        session.send("/app/drink-items/delete/" + id, null);

        returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with " + id + " are successfully deleted!", returnDTO.getMessage());

    }

    @Test
    public void delete_InvalidId_DeletedObject() throws Exception {

        session.send("/app/drink-items/delete/1", null);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with the id 1 are not found in the database.", returnDTO.getMessage());
    }

    @Test
    public void delete_InvalidState_DeletedObject() throws Exception {

        session.send("/app/drink-items/delete/8", null);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with state of ready cannot be deleted.", returnDTO.getMessage());
    }
}
