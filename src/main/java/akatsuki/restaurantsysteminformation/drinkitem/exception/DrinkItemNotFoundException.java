package akatsuki.restaurantsysteminformation.drinkitem.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class DrinkItemNotFoundException extends NotFoundRuntimeException {
    public DrinkItemNotFoundException(String message) {
        super(message);
    }
}
