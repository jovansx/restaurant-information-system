package akatsuki.restaurantsysteminformation.order.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderDeletionException extends RuntimeException {
    public OrderDeletionException(String message) {
        super(message);
    }
}
