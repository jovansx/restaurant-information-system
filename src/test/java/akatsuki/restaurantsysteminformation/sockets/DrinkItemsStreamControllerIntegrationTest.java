package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsActionRequestDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
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

        int size = drinkItemsService.getAll().size();
        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, List.of(new DrinkItemCreateDTO(5, 1L)), null);

        session.send("/app/drink-items/create", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        List<DrinkItems> drinkItems = drinkItemsService.getAll();

        assertNotNull(returnDTO);
        assertEquals(size + 1, drinkItems.size());
        assertTrue(returnDTO.isSuccessfullyFinished());

        drinkItemsService.deleteById(drinkItems.get(drinkItems.size() - 1).getId());
    }

    @Test
    public void create_InvalidOrderId_ExceptionThrown() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(44, List.of(new DrinkItemCreateDTO(5, 1L)), null);

        session.send("/app/drink-items/create", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Order with the id 44 is not found in the database.", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void create_InvalidItemId_ExceptionThrown() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, List.of(new DrinkItemCreateDTO(5, 4L)), null);

        session.send("/app/drink-items/create", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Not correct type of drink item!", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void update_Valid_UpdatedObject() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, List.of(new DrinkItemCreateDTO(5, 1L)), null);

        session.send("/app/drink-items/update/6", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with 6 are successfully updated!", returnDTO.getMessage());

        dto = new DrinkItemsCreateDTO(1, List.of(new DrinkItemCreateDTO(3, 1L)), null);
        drinkItemsService.update(dto, 6);

        Order order = orderService.getOneWithAll(1L);
        order.setTotalPrice(8);
        orderService.save(order);
    }

    @Test
    public void update_OrderNotContainingItem_ExceptionThrown() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(2, List.of(new DrinkItemCreateDTO(5, 1L)), null);

        session.send("/app/drink-items/update/6", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items list with id 6 is not contained within order drinks.", returnDTO.getMessage());
    }

    @Test
    public void update_InvalidType_ExceptionThrown() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, List.of(new DrinkItemCreateDTO(5, 1L)), null);

        session.send("/app/drink-items/update/1", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with the id 1 are not found in the database.", returnDTO.getMessage());
    }

    @Test
    public void update_InvalidDrinkItemsState_ExceptionThrown() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, List.of(new DrinkItemCreateDTO(5, 1L)), null);

        session.send("/app/drink-items/update/8", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Cannot change drink items list, because its state is ready. Allowed state to change is 'on_hold'.", returnDTO.getMessage());
    }

    @Test
    public void update_InvalidItemId_ExceptionThrown() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(1, List.of(new DrinkItemCreateDTO(5, 4L)), null);

        session.send("/app/drink-items/update/6", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Not correct type of drink item!", returnDTO.getMessage());
    }

    @Test
    public void update_InvalidOrderId_ExceptionThrown() throws Exception {

        DrinkItemsCreateDTO dto = new DrinkItemsCreateDTO(44, List.of(new DrinkItemCreateDTO(5, 4L)), null);

        session.send("/app/drink-items/update/6", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Order with the id 44 is not found in the database.", returnDTO.getMessage());
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

        DrinkItems drinkItems = drinkItemsService.getOne(6L);
        session.send("/app/drink-items/delete/6", null);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());
        assertEquals("Drink items with 6 are successfully deleted!", returnDTO.getMessage());

        Order order = orderService.getOneWithAll(1L);
        drinkItems.setActive(true);
        drinkItems.setDeleted(false);
        drinkItemsService.save(drinkItems);
        order.getDrinks().add(drinkItems);
        order.setTotalPrice(8);
        orderService.save(order);
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
