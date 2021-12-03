package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.sockets.dto.SocketResponseDTO;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;

public class MyStompFrameHandler implements StompFrameHandler {

    private final BlockingQueue<SocketResponseDTO> queue;

    public MyStompFrameHandler(BlockingQueue<SocketResponseDTO> queue) {
        this.queue = queue;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return SocketResponseDTO.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        queue.add((SocketResponseDTO) payload);
    }
}
