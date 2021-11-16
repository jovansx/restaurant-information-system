package akatsuki.restaurantsysteminformation.itemcategory.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class ItemCategoryDeleteException extends ConflictRuntimeException {
    public ItemCategoryDeleteException(String message) {
        super(message);
    }
}
