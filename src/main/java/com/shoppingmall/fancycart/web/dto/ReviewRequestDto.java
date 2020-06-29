package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class ReviewRequestDto {

    private Long userId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Min(value = 0)
    @Max(value = 5)
    private int rate;

    @Getter
    public static class Get {
        private Integer page;

        @Builder
        public Get(Integer page) {
            this.page = page;
        }
    }
}
