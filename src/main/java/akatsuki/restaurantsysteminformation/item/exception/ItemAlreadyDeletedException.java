package akatsuki.restaurantsysteminformation.item.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class ItemAlreadyDeletedException extends ConflictRuntimeException {
    public ItemAlreadyDeletedException(String message) {
        super(message);
    }
}
