package io.github.adrbloch.bookcatalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class FieldsNotMatchException extends RuntimeException {

    public FieldsNotMatchException(String message) {
        super(message);
    }

}
