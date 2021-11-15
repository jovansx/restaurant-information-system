package akatsuki.restaurantsysteminformation.room.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class RoomDeletionFailedException extends ConflictRuntimeException {
    public RoomDeletionFailedException(String message) {
        super(message);
    }
}
