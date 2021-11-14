package akatsuki.restaurantsysteminformation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * Method that handles thrown exception by spring validator.
     * Validations used at DTO classes to check attributes, as are @NotNUll, @Size, @Min, ...
     *
     * @param exception - MethodArgumentNotValidException
     * @return ExceptionResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgNotValid(MethodArgumentNotValidException exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getLocalizedMessage());
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> errors = bindingResult.getFieldErrors();
        if (!errors.isEmpty()) {
            response.setValidationErrors(new HashMap<>());
            response.setMessage(null);
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                response.addPairToMap(fieldError);
            }
        }
        return response;
    }

    @ExceptionHandler(NotFoundRuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundRuntimeException exception) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(ConflictRuntimeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleConflictException(ConflictRuntimeException exception) {
        return new ExceptionResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }
}
