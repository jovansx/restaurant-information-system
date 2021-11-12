package akatsuki.restaurantsysteminformation.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemExistsException extends RuntimeException {
    public ItemExistsException() {
        super();
    }

    public ItemExistsException(String message) {
        super(message);
    }

    public ItemExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
