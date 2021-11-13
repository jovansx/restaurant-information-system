package akatsuki.restaurantsysteminformation.dishitem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DishItemInvalidStateException extends RuntimeException {
    public DishItemInvalidStateException(String message) {
        super(message);
    }
}
