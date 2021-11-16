package akatsuki.restaurantsysteminformation.item.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class ItemExistsException extends ConflictRuntimeException {
    public ItemExistsException(String message) {
        super(message);
    }
}
