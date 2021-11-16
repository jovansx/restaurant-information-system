package akatsuki.restaurantsysteminformation.itemcategory.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class ItemCategoryNotFoundException extends NotFoundRuntimeException {
    public ItemCategoryNotFoundException(String message) {
        super(message);
    }
}
