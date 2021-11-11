package akatsuki.restaurantsysteminformation.restauranttable.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RestaurantTableShapeNotValidException extends RuntimeException {
    public RestaurantTableShapeNotValidException(String message) {
        super(message);
    }
}
