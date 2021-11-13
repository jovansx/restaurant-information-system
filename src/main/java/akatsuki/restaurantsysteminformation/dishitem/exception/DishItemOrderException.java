package akatsuki.restaurantsysteminformation.dishitem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DishItemOrderException extends RuntimeException {
    public DishItemOrderException(String message) {
        super(message);
    }
}
