package akatsuki.restaurantsysteminformation.itemcategory.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class ItemCategoryNameException extends ConflictRuntimeException {
    public ItemCategoryNameException(String message) {
        super(message);
    }
}
