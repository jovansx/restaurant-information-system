package akatsuki.restaurantsysteminformation.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemCodeNotValidException extends RuntimeException {
    public ItemCodeNotValidException() {
        super();
    }

    public ItemCodeNotValidException(String message) {
        super(message);
    }

    public ItemCodeNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
