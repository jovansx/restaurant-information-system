package akatsuki.restaurantsysteminformation.drinkitems.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class DrinkItemsNotFoundException extends NotFoundRuntimeException {
    public DrinkItemsNotFoundException(String message) {
        super(message);
    }
}
