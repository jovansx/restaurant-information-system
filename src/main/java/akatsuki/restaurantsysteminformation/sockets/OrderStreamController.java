package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.sockets.dto.SocketResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;

@Controller
@RequiredArgsConstructor
@Validated
public class OrderStreamController {

    private final OrderService orderService;

    @MessageMapping({"/order/discard/{id}"})
    @SendTo("/topic/order")
    public SocketResponseDTO discard(@DestinationVariable @Positive(message = "Id has to be a positive value.") long id) {
        orderService.discard(id);
        return new SocketResponseDTO(true, "Order with id " + id + " is successfully discarded!", "");
    }

    @MessageMapping({"/order/charge/{id}"})
    @SendTo("/topic/order")
    public SocketResponseDTO charge(@DestinationVariable @Positive(message = "Id has to be a positive value.") long id) {
        orderService.charge(id);
        return new SocketResponseDTO(true, "Order with id " + id + " is successfully charged!", "");
    }

    @MessageExceptionHandler
    @SendTo("/topic/order")
    public SocketResponseDTO handleException(RuntimeException exception) {
        return new SocketResponseDTO(false, exception.getLocalizedMessage(), "");
    }
}
