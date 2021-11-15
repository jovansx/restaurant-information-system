package akatsuki.restaurantsysteminformation.registereduser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegisteredUserDeleteException extends RuntimeException {
    public RegisteredUserDeleteException(String message) {
        super(message);
    }
}
