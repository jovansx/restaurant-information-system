package akatsuki.restaurantsysteminformation.restauranttable.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RestaurantTableNotAvailableException extends ConflictRuntimeException {
    public RestaurantTableNotAvailableException(String message) {
        super(message);
    }
}
