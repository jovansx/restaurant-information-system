package akatsuki.restaurantsysteminformation.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserTypeNotValidException extends RuntimeException {

    public UserTypeNotValidException(String message) {
        super(message);
    }

}
