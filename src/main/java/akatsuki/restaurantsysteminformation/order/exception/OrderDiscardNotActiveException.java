package akatsuki.restaurantsysteminformation.order.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class OrderDiscardNotActiveException extends ConflictRuntimeException {
    public OrderDiscardNotActiveException(String message) {
        super(message);
    }
}
