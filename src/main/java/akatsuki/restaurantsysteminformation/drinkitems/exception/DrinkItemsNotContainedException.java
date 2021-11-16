package akatsuki.restaurantsysteminformation.drinkitems.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class DrinkItemsNotContainedException extends ConflictRuntimeException {
    public DrinkItemsNotContainedException(String message) {
        super(message);
    }
}
