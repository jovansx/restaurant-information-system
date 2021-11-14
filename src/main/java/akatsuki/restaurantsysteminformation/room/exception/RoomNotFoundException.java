package akatsuki.restaurantsysteminformation.room.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class RoomNotFoundException extends NotFoundRuntimeException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
