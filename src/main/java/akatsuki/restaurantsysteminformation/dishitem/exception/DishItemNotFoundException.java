package akatsuki.restaurantsysteminformation.dishitem.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class DishItemNotFoundException extends NotFoundRuntimeException {
    public DishItemNotFoundException(String message) {
        super(message);
    }
}
