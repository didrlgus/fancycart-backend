package com.shoppingmall.fancycart.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UserResponseDto {
    @Getter
    @Builder
    public static class Profile {
        private String name;
        private String email;
        private LocalDate birth;
        private String agreeMessageByEmail;
        private String roadAddr;
        private String buildingName;
        private String detailAddr;
    }
}
