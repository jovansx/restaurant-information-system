package akatsuki.restaurantsysteminformation.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserDeletedException extends RuntimeException {
    public UserDeletedException() {
        super();
    }

    public UserDeletedException(String message) {
        super(message);
    }

    public UserDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
