package akatsuki.restaurantsysteminformation.user.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class UserTypeNotValidException extends ConflictRuntimeException {
    public UserTypeNotValidException(String message) {
        super(message);
    }
}
