package akatsuki.restaurantsysteminformation.item.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class ItemCodeNotValidException extends ConflictRuntimeException {
    public ItemCodeNotValidException(String message) {
        super(message);
    }
}
