package akatsuki.restaurantsysteminformation.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderDiscardException extends RuntimeException {
    public OrderDiscardException(String message) {
        super(message);
    }
}
