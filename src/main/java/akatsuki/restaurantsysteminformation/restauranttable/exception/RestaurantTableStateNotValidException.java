package akatsuki.restaurantsysteminformation.restauranttable.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RestaurantTableStateNotValidException extends RuntimeException {
    public RestaurantTableStateNotValidException(String message) {
        super(message);
    }
}
