package com.shoppingmall.fancycart.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIsAvailable(char availableCategory);

    Long countByCatLv(Integer upperCategoryLv);

    Long countByCatLvAndUpprCatCd(Integer lowerCategoryLv, String upprCatCd);

    List<Category> findByCatLvOrderByCreatedDateDesc(int i);

    List<Category> findByCatLvOrderByModifiedDateDesc(int catLv);
}
