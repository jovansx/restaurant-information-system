package akatsuki.restaurantsysteminformation.user.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class UserDeletedException extends ConflictRuntimeException {
    public UserDeletedException(String message) {
        super(message);
    }
}
