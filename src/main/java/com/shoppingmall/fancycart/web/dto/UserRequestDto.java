package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

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

    @Getter
    public static class Update {
        @NotBlank
        @Length(max = 50)
        private String name;

        private LocalDate birth;

        @NotBlank
        private String agreeMessageByEmail;

        @Length(max = 50)
        private String roadAddr;
        @Length(max = 50)
        private String buildingName;
        @Length(max = 50)
        private String detailAddr;

        @Builder
        public Update(String name, LocalDate birth, String agreeMessageByEmail,
                      String roadAddr, String buildingName, String detailAddr) {
            this.name = name;
            this.birth = birth;
            this.agreeMessageByEmail = agreeMessageByEmail;
            this.roadAddr = roadAddr;
            this.buildingName = buildingName;
            this.detailAddr = detailAddr;
        }
    }
}
