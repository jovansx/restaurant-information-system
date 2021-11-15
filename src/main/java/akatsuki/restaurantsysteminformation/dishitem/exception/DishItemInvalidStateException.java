package akatsuki.restaurantsysteminformation.dishitem.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class DishItemInvalidStateException extends ConflictRuntimeException {
    public DishItemInvalidStateException(String message) {
        super(message);
    }
}
