package akatsuki.restaurantsysteminformation.dishitem.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class DishItemInvalidTypeException extends ConflictRuntimeException {
    public DishItemInvalidTypeException(String message) {
        super(message);
    }
}
