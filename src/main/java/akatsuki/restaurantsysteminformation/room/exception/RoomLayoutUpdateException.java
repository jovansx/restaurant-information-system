package akatsuki.restaurantsysteminformation.room.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RoomLayoutUpdateException extends ConflictRuntimeException {
    public RoomLayoutUpdateException(String message) {
        super(message);
    }
}
