package akatsuki.restaurantsysteminformation.restauranttable.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RestaurantTableDeletedException extends ConflictRuntimeException {
    public RestaurantTableDeletedException(String message) {
        super(message);
    }
}
