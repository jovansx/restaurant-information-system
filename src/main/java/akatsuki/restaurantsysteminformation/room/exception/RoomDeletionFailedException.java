package akatsuki.restaurantsysteminformation.room.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoomDeletionFailedException extends RuntimeException{
    public RoomDeletionFailedException(String message) {super(message);}
}
