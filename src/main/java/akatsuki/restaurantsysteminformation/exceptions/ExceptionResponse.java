package akatsuki.restaurantsysteminformation.exceptions;

import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.Map;

public class ExceptionResponse {
    private int status;
    private long timestamp;
    private String message;
    private Map<String, String> validationErrors;

    public ExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date().getTime();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public void addPairToMap(FieldError fieldError) {
        validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
