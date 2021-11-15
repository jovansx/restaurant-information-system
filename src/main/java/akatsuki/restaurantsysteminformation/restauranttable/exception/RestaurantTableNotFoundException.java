package akatsuki.restaurantsysteminformation.restauranttable.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class RestaurantTableNotFoundException extends NotFoundRuntimeException {
    public RestaurantTableNotFoundException(String message) {
        super(message);
    }
}
