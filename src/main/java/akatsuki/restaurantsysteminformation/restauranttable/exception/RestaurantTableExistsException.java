package akatsuki.restaurantsysteminformation.restauranttable.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RestaurantTableExistsException extends ConflictRuntimeException {
    public RestaurantTableExistsException(String message) {
        super(message);
    }
}
