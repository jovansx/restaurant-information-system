package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.dishitem.DishItemService;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemActionRequestDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemUpdateDTO;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemInvalidStateException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemOrderException;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.sockets.dto.SocketResponseDTO;
import org.junit.jupiter.api.Assertions;
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
public class DishItemStreamControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private DishItemService dishItemService;

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
        session.subscribe("/topic/dish-item", new MyStompFrameHandler(blockingQueue));
    }

    @Test
    public void create_Valid_SavedObject() throws Exception {

        DishItemCreateDTO dto = new DishItemCreateDTO(4L, 2, null, 2L, null);

        session.send("/app/dish-item/create", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());

        dishItemService.deleteById(returnDTO.getId());
    }

    @Test
    public void create_InvalidOrderId_ExceptionThrown() throws Exception {

        DishItemCreateDTO dto = new DishItemCreateDTO(4L, 2, null, 8000L, null);

        session.send("/app/dish-item/create", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Order with the id 8000 is not found in the database.", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void create_InvalidItemId_ExceptionThrown() throws Exception {

        DishItemCreateDTO dto = new DishItemCreateDTO(1L, 2, null, 2L, null);

        session.send("/app/dish-item/create", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertEquals("Item type is not DISH.", returnDTO.getMessage());
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    // TODO napravi i za kreiran order

    @Test
    public void update_Valid_UpdatedObject() throws Exception {

        DishItemUpdateDTO dto = new DishItemUpdateDTO(1L, 7, "New note.", 1L);

        session.send("/app/dish-item/update/1", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());
        assertEquals("Dish item with 1 is successfully updated!", returnDTO.getMessage());

        dto = new DishItemUpdateDTO(4L, 1, null, 1L);
        dishItemService.update(dto, 1);

        Order order = orderService.getOneWithAll(1L);
        order.setTotalPrice(8);
        orderService.save(order);
    }

    @Test
    public void update_ItemNotExist_ExceptionThrown() throws Exception {
        DishItemUpdateDTO dto = new DishItemUpdateDTO(15L, 10, "New note.", 1L);
        session.send("/app/dish-item/update/15", dto);
        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Dish item with the id 15 is not found in the database.", returnDTO.getMessage());
    }

    @Test
    public void update_InvalidDishItem_ExceptionThrown() throws Exception {
        DishItemUpdateDTO dto = new DishItemUpdateDTO(1L, 10, "New note.", 2L);

        session.send("/app/dish-item/update/1", dto);
        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Dish item order id is not equal to 2. Order cannot be changed.", returnDTO.getMessage());
    }

    @Test
    public void update_InvalidItemState_ExceptionThrown() throws Exception {
        DishItemUpdateDTO dto = new DishItemUpdateDTO(2L, 10, "New note.", 1L);

        session.send("/app/dish-item/update/2", dto);
        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Cannot change dish item, because its state is preparation.", returnDTO.getMessage());
    }


    @Test
    public void update_InvalidOrderId_ExceptionThrown() throws Exception {

        DishItemUpdateDTO dto = new DishItemUpdateDTO(4L, 5, null, 100L);

        session.send("/app/dish-item/update/4", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Order with the id 100 is not found in the database.", returnDTO.getMessage());
    }

    @Test
    public void changeStateOfDishItem_OnHoldToPreparation_SavedObject() throws Exception {

        DishItemActionRequestDTO dto = new DishItemActionRequestDTO(3L, 1L);

        session.send("/app/dish-item/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());

        DishItem dishItem = dishItemService.findOneActiveAndFetchItemAndChef(1L);
        dishItem.setState(ItemState.ON_HOLD);
        dishItem.setChef(null);
        dishItemService.save(dishItem);
    }

    @Test
    public void changeStateOfDishItem_PreparationToReady_SavedObject() throws Exception {

        DishItemActionRequestDTO dto = new DishItemActionRequestDTO(3L, 2L);

        session.send("/app/dish-item/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());

        DishItem dishItem = dishItemService.findOneActiveAndFetchItemAndChef(2L);
        dishItem.setState(ItemState.PREPARATION);
        dishItemService.save(dishItem);
    }

    @Test
    public void changeStateOfDishItem_ReadyToDelivered_SavedObject() throws Exception {

        DishItemActionRequestDTO dto = new DishItemActionRequestDTO(1L, 3L);

        session.send("/app/dish-item/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());

        DishItem dishItem = dishItemService.findOneActiveAndFetchItemAndChef(3L);
        dishItem.setState(ItemState.READY);
        dishItemService.save(dishItem);
    }

    @Test
    public void changeStateOfDishItem_DrinkItemsId_ExceptionThrown() throws Exception {

        DishItemActionRequestDTO dto = new DishItemActionRequestDTO(1L, 6L);

        session.send("/app/dish-item/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Dish item with the id 6 is not found in the database.", returnDTO.getMessage());
    }

    @Test
    public void changeStateOfDishItem_WrongUserType_ExceptionThrown() throws Exception {

        DishItemActionRequestDTO dto = new DishItemActionRequestDTO(3L, 3L);

        session.send("/app/dish-item/change-state", dto);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("User with the id 3 is not a waiter.", returnDTO.getMessage());
    }

    @Test
    public void delete_Valid_DeletedObject() throws Exception {

        DishItem dishItem = dishItemService.findOneActiveAndFetchItemAndChef(1L);
        session.send("/app/dish-item/delete/1", null);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertTrue(returnDTO.isSuccessfullyFinished());
        assertEquals("Dish item state is successfully deleted!", returnDTO.getMessage());

        Order order = orderService.getOneWithAll(1L);
        dishItem.setActive(true);
        dishItem.setDeleted(false);
        dishItemService.save(dishItem);
        order.getDishes().add(dishItem);
        order.setTotalPrice(8);
        orderService.save(order);
    }

    @Test
    public void delete_InvalidId_DeletedObject() throws Exception {

        session.send("/app/dish-item/delete/5", null);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
        assertEquals("Dish item with the id 5 is not found in the database.", returnDTO.getMessage());
    }
}
