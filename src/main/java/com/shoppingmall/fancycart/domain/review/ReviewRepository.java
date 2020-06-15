package com.shoppingmall.fancycart.domain.review;

import com.shoppingmall.fancycart.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.rate) AS rateAvg " +
            "FROM Review r " +
            "WHERE r.product = ?1")
    double getReviewRateAvg(Product product);
}
