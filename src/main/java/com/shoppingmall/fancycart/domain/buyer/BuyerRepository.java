package com.shoppingmall.fancycart.domain.buyer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    List<Buyer> findByUserIdAndProductId(Long userId, Long productId);
}
