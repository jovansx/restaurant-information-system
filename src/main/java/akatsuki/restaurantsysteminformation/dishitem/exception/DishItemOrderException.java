package akatsuki.restaurantsysteminformation.dishitem.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class DishItemOrderException extends ConflictRuntimeException {
    public DishItemOrderException(String message) {
        super(message);
    }
}
