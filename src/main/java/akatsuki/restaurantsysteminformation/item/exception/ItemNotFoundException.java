package akatsuki.restaurantsysteminformation.item.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class ItemNotFoundException extends NotFoundRuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
