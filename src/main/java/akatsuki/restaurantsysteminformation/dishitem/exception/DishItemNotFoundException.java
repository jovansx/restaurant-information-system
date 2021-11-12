package akatsuki.restaurantsysteminformation.dishitem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DishItemNotFoundException extends RuntimeException {
    public DishItemNotFoundException() {
        super();
    }

    public DishItemNotFoundException(String message) {
        super(message);
    }

    public DishItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
