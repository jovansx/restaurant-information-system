package akatsuki.restaurantsysteminformation.restauranttable.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RestaurantTableDeletedException extends RuntimeException{
    public RestaurantTableDeletedException(String message) {
        super(message);
    }
}
