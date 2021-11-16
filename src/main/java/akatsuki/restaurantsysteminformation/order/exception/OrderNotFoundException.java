package akatsuki.restaurantsysteminformation.order.exception;

import akatsuki.restaurantsysteminformation.exceptions.NotFoundRuntimeException;

public class OrderNotFoundException extends NotFoundRuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
