package akatsuki.restaurantsysteminformation.itemcategory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ItemCategoryDeleteException extends RuntimeException {
    public ItemCategoryDeleteException(String message) {
        super(message);
    }
}
