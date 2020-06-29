package com.shoppingmall.fancycart.web.dto;

import com.shoppingmall.fancycart.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private Long id;

    private String title;

    private String content;

    private int rate;

    private UserProfileResponseDto.Review userReviewResponseDto;

    @Builder
    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.userReviewResponseDto = review.getUser().toUserReviewResponseDto(review.getUser());
    }
}
