package akatsuki.restaurantsysteminformation.user.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class UserNotFoundException extends NotFoundRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
