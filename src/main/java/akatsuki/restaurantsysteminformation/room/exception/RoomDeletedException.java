package akatsuki.restaurantsysteminformation.room.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RoomDeletedException extends ConflictRuntimeException {
    public RoomDeletedException(String message) {
        super(message);
    }
}
