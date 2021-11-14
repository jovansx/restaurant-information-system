package akatsuki.restaurantsysteminformation.dishitem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DishItemAlreadyDeletedException extends RuntimeException {
    public DishItemAlreadyDeletedException(String message) {
        super(message);
    }
}
