package akatsuki.restaurantsysteminformation.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemAlreadyDeletedException extends RuntimeException {

    public ItemAlreadyDeletedException(String message) {
        super(message);
    }

}
