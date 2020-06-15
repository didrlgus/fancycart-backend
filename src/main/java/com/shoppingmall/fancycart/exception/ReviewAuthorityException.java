package com.shoppingmall.fancycart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReviewAuthorityException extends RuntimeException {
    public ReviewAuthorityException(String message) {
        super(message);
    }
}
