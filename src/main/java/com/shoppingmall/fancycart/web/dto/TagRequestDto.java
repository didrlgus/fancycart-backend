package com.shoppingmall.fancycart.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter
public class TagRequestDto {

    @NotBlank
    @Length(max = 50)
    private String title;
    @Builder
    public TagRequestDto(String title) {
        this.title = title;
    }
}
