package akatsuki.restaurantsysteminformation.restauranttable.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantTableNotFoundException extends RuntimeException {
    public RestaurantTableNotFoundException(String message) {
        super(message);
    }
}
