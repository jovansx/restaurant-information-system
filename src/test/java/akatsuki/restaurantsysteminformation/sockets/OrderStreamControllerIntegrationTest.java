package akatsuki.restaurantsysteminformation.sockets;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void discard_OrderNotFound_ExceptionThrown() throws Exception {
        BlockingQueue<SocketResponseDTO> blockingQueue = new ArrayBlockingQueue(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(String.format("ws://localhost:%d/app/stomp", port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/order", new MyStompFrameHandler(blockingQueue));

        session.send("/app/order/discard/44", null);
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

}
