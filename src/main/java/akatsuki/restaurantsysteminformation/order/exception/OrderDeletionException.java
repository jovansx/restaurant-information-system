package akatsuki.restaurantsysteminformation.order.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class OrderDeletionException extends ConflictRuntimeException {
    public OrderDeletionException(String message) {
        super(message);
    }
}
