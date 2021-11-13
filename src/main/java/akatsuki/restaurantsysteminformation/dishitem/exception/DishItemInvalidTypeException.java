package akatsuki.restaurantsysteminformation.dishitem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DishItemInvalidTypeException extends RuntimeException {
    public DishItemInvalidTypeException(String message) {
        super(message);
    }
}
