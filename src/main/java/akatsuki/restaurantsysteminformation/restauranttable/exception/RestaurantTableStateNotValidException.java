package akatsuki.restaurantsysteminformation.restauranttable.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RestaurantTableStateNotValidException extends ConflictRuntimeException {
    public RestaurantTableStateNotValidException(String message) {
        super(message);
    }
}
