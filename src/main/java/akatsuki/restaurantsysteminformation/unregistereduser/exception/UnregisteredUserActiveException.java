package akatsuki.restaurantsysteminformation.unregistereduser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UnregisteredUserActiveException extends RuntimeException {
    public UnregisteredUserActiveException(String message) {
        super(message);
    }
}
