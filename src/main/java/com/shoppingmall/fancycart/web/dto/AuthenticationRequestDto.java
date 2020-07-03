package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
