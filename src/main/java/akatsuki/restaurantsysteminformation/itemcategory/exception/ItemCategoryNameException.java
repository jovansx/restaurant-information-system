package akatsuki.restaurantsysteminformation.itemcategory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemCategoryNameException extends RuntimeException {
    public ItemCategoryNameException(String message) {
        super(message);
    }
}
