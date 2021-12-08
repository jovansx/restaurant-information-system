package akatsuki.restaurantsysteminformation.order.exception;

import akatsuki.restaurantsysteminformation.exceptions.ConflictRuntimeException;

public class OrderWaiterNotValidException extends ConflictRuntimeException {
    public OrderWaiterNotValidException(String message) {
        super(message);
    }
}
