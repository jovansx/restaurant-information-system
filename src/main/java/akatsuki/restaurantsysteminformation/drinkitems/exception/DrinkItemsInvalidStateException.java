package akatsuki.restaurantsysteminformation.drinkitems.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class DrinkItemsInvalidStateException extends ConflictRuntimeException {
    public DrinkItemsInvalidStateException(String message) {
        super(message);
    }
}
