package akatsuki.restaurantsysteminformation.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderDiscardException extends RuntimeException {
    public OrderDiscardException() {
        super();
    }

    public OrderDiscardException(String message) {
        super(message);
    }

    public OrderDiscardException(String message, Throwable cause) {
        super(message, cause);
    }
}
