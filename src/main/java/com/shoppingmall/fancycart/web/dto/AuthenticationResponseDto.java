package com.shoppingmall.fancycart.web.dto;

import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
public class AuthenticationResponseDto {
    private Long id;
    private String token;

    @ConstructorProperties({"id", "token"})
    public AuthenticationResponseDto(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
