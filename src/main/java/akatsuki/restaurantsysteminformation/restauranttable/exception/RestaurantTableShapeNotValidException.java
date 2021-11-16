package akatsuki.restaurantsysteminformation.restauranttable.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RestaurantTableShapeNotValidException extends ConflictRuntimeException {
    public RestaurantTableShapeNotValidException(String message) {
        super(message);
    }
}
