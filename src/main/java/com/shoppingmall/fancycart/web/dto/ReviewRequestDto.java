package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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

}
