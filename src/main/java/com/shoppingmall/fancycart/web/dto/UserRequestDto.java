package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
public class UserRequestDto {

    @Getter
    public static class Post {
        @NotBlank
        @Length(max = 50)
        private String name;

        @Email
        @Length(max = 50)
        private String email;

        @NotBlank
        @Length(max = 50)
        private String password;

        @Builder
        public Post(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
}
