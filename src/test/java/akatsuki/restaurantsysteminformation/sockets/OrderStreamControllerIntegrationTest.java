package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.order.dto.OrderCreateDTO;
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
public class OrderStreamControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient webSocketStompClient;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    public void create_Valid_SavedObject() throws Exception {
        int size = orderService.getAllActive().size();

        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));
        OrderCreateDTO dto = new OrderCreateDTO(1L);

        session.send("/app/order/create", dto);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        List<Order> orders = orderService.getAllActive();

        assertNotNull(returnDTO);
        assertEquals(size + 1, orders.size());
        assertTrue(returnDTO.isSuccessfullyFinished());

        orderService.delete(orders.get(orders.size() - 1).getId());
    }

    @Test
    public void create_InvalidWaiterId_ExceptionThrown() throws Exception {
        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));
        OrderCreateDTO dto = new OrderCreateDTO(2L);

        session.send("/app/order/create", dto);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void discard_Valid_DiscardObject() throws Exception {
        Order order = orderService.create(new OrderCreateDTO(1L));

        int size = orderService.getAllActive().size();

        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/discard/" + order.getId(), null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        List<Order> orders = orderService.getAllActive();

        assertNotNull(returnDTO);
        assertEquals(size - 1, orders.size());
        assertTrue(returnDTO.isSuccessfullyFinished());

        orderService.delete(order.getId());
    }

    @Test
    public void discard_AlreadyDiscarded_ExceptionThrown() throws Exception {
        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/discard/4", null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void discard_NotActive_ExceptionThrown() throws Exception {
        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/discard/5", null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void charge_Valid_DiscardObject() throws Exception {
        Order order = orderService.create(new OrderCreateDTO(1L));

        int size = orderService.getAllActive().size();

        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/charge/" + order.getId(), null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        List<Order> orders = orderService.getAllActive();

        assertNotNull(returnDTO);
        assertEquals(size - 1, orders.size());
        assertTrue(returnDTO.isSuccessfullyFinished());

        orderService.delete(order.getId());
    }

    @Test
    public void charge_AlreadyDiscarded_ExceptionThrown() throws Exception {
        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/charge/4", null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void charge_NotActive_ExceptionThrown() throws Exception {
        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/charge/5", null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void delete_HaveItems_ExceptionThrown() throws Exception {
        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/delete/1", null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        assertNotNull(returnDTO);
        assertFalse(returnDTO.isSuccessfullyFinished());
    }

    @Test
    public void delete_Valid_DeletedObject() throws Exception {
        Order order = orderService.create(new OrderCreateDTO(1L));

        int size = orderService.getAllActive().size();

        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/delete/" + order.getId(), null);
        Thread.sleep(100);

        SocketResponseDTO returnDTO = blockingQueue.poll(1, SECONDS);

        List<Order> orders = orderService.getAllActive();

        assertNotNull(returnDTO);
        assertEquals(size - 1, orders.size());
        assertTrue(returnDTO.isSuccessfullyFinished());
    }

}
