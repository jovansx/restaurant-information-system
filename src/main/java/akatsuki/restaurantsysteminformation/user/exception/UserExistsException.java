package akatsuki.restaurantsysteminformation.user.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class UserExistsException extends ConflictRuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
