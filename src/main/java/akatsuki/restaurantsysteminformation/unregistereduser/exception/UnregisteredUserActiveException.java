package akatsuki.restaurantsysteminformation.unregistereduser.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class UnregisteredUserActiveException extends ConflictRuntimeException {
    public UnregisteredUserActiveException(String message) {
        super(message);
    }
}
