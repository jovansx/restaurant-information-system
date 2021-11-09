package akatsuki.restaurantsysteminformation.drinkitems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DrinkItemsNotFoundException extends RuntimeException {
    public DrinkItemsNotFoundException() {
        super();
    }

    public DrinkItemsNotFoundException(String message) {
        super(message);
    }

    public DrinkItemsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
