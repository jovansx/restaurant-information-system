package akatsuki.restaurantsysteminformation.registereduser.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RegisteredUserDeleteException extends ConflictRuntimeException {
    public RegisteredUserDeleteException(String message) {
        super(message);
    }
}
