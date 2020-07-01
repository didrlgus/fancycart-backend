package com.shoppingmall.fancycart.handler;

import com.shoppingmall.fancycart.excepaion.ErrorResponse;
import com.shoppingmall.fancycart.validator.ValidCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.shoppingmall.fancycart.utils.ExceptionUtils.INPUT_EXCEPTION_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestControllerHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        final BindingResult bindingResult = e.getBindingResult();
        final List<FieldError> errors = bindingResult.getFieldErrors();

        return buildFieldErrors(
                INPUT_EXCEPTION_MESSAGE,
                errors.parallelStream()
                        .map(error -> ErrorResponse.FieldError.builder()
                                .reason(error.getDefaultMessage())
                                .field(error.getField())
                                .value((String) error.getRejectedValue())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> runtimeExceptionHandler(HttpServletRequest request, ValidCustomException exception) {
        return new ResponseEntity<>(exception.getErrors()[0].getDefaultMessage(), BAD_REQUEST);
    }

    private ErrorResponse buildFieldErrors(String message, List<ErrorResponse.FieldError> errors) {
        return ErrorResponse.builder()
                .message(message)
                .errors(errors)
                .build();
    }
}
