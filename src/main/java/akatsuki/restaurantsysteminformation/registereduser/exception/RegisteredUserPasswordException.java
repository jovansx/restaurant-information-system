package akatsuki.restaurantsysteminformation.registereduser.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RegisteredUserPasswordException extends ConflictRuntimeException {
    public RegisteredUserPasswordException(String message) {
        super(message);
    }
}
