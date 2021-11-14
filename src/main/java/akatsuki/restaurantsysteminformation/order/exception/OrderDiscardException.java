package akatsuki.restaurantsysteminformation.order.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class OrderDiscardException extends ConflictRuntimeException {
    public OrderDiscardException(String message) {
        super(message);
    }
}
