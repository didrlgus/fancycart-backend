package com.shoppingmall.fancycart.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InValidCategoryException extends RuntimeException {
    public InValidCategoryException(String message) {
        super(message);
    }
}
