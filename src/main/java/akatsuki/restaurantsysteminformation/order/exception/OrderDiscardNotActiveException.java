package akatsuki.restaurantsysteminformation.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderDiscardNotActiveException extends RuntimeException {
    public OrderDiscardNotActiveException(String message) {
        super(message);
    }
}
