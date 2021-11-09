package akatsuki.restaurantsysteminformation.room.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoomDeletedException extends RuntimeException {
    public RoomDeletedException(String message) {
        super(message);
    }
}
