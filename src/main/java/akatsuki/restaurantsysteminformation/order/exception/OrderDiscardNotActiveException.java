package akatsuki.restaurantsysteminformation.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderDiscardNotActiveException extends RuntimeException {
    public OrderDiscardNotActiveException() {
        super();
    }

    public OrderDiscardNotActiveException(String message) {
        super(message);
    }

    public OrderDiscardNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
