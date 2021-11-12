package akatsuki.restaurantsysteminformation.itemcategory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemCategoryNotFoundException extends RuntimeException {
    public ItemCategoryNotFoundException() {
        super();
    }

    public ItemCategoryNotFoundException(String message) {
        super(message);
    }

    public ItemCategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
