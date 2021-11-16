package akatsuki.restaurantsysteminformation.room.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RoomExistsException extends ConflictRuntimeException {
    public RoomExistsException(String message) {
        super(message);
    }
}
