package akatsuki.restaurantsysteminformation.drinkitems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DrinkItemsNotFoundException extends RuntimeException {
    public DrinkItemsNotFoundException(String message) {
        super(message);
    }
}
