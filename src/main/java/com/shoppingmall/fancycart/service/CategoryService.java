package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.category.Category;
import com.shoppingmall.fancycart.domain.category.CategoryRepository;
import com.shoppingmall.fancycart.domain.tag.Tag;
import com.shoppingmall.fancycart.utils.ExceptionUtils;
import com.shoppingmall.fancycart.web.dto.CategoryRequestDto;
import com.shoppingmall.fancycart.web.dto.CategoryResponseDto;
import com.shoppingmall.fancycart.web.dto.CategoryUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private static final char AVAILABLE_CATEGORY = 'Y';
    private static final Integer UPPER_CATEGORY_LV = 1;
    private static final Integer LOWER_CATEGORY_LV = 2;

    public List<CategoryResponseDto> getCategory() {
        List<Category> categoryList = categoryRepository.findAllByIsAvailable(AVAILABLE_CATEGORY);
        return toCategoryResponseDtoList(categoryList);
    }

    public void addCategory(CategoryRequestDto categoryRequestDto) {
        categoryRepository.save(makeCategory(categoryRequestDto));
    }

    public void updateCategory(Long id, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        Category category = categoryOpt.orElseThrow(() -> new NoSuchElementException(ExceptionUtils.NO_EXIST_USER_MESSAGE));
        category = category.update(categoryUpdateRequestDto.getCatNm(), categoryUpdateRequestDto.getIsAvailable());
        categoryRepository.save(category);
    }

    private Category makeCategory(CategoryRequestDto categoryRequestDto) {
        String catCd = "";
        if(categoryRequestDto.getCatLv().equals(UPPER_CATEGORY_LV)) {
            catCd = getUpperCatCd();
        }
        if(categoryRequestDto.getCatLv().equals(LOWER_CATEGORY_LV)) {
            catCd = getLowerCatCd(categoryRequestDto);
        }

        return Category.builder()
                .catCd(catCd)
                .catNm(categoryRequestDto.getCatNm())
                .catLv(categoryRequestDto.getCatLv())
                .isAvailable(categoryRequestDto.getIsAvailable())
                .upprCatCd(categoryRequestDto.getUpprCatCd())
                .tag(new Tag(categoryRequestDto.getCatNm()))
                .build();
    }

    private String getLowerCatCd(CategoryRequestDto categoryRequestDto) {
        String upperCatCd = categoryRequestDto.getUpprCatCd();

        StringBuilder lowerCatCd = new StringBuilder();
        // ex) C001000 -> C001
        lowerCatCd.append(upperCatCd, 0, 4);

        Long lowerCategoryCount = categoryRepository.countByCatLvAndUpprCatCd(LOWER_CATEGORY_LV, upperCatCd);
        lowerCategoryCount++;

        return getLowerCatCdFromCatCnt(lowerCatCd, lowerCategoryCount.intValue());
    }

    private String getUpperCatCd() {
        Long upperCategoryCount = categoryRepository.countByCatLv(UPPER_CATEGORY_LV);
        upperCategoryCount++;

        StringBuilder upperCatCd = new StringBuilder("C");

        return getUpperCatCdFromCatCnt(upperCatCd, upperCategoryCount.intValue());
    }

    private String getLowerCatCdFromCatCnt(StringBuilder lowerCatCd, int lowerCategoryCount) {
        // 하위 카테고리 - 1~999까지
        if(lowerCategoryCount<10) {
            return lowerCatCd.append("00").append(lowerCategoryCount).toString();
        }
        if(lowerCategoryCount<100) {
            return lowerCatCd.append('0').append(lowerCategoryCount).toString();
        }
        return lowerCatCd.append(lowerCategoryCount).toString();
    }

    private String getUpperCatCdFromCatCnt(StringBuilder upprCatCd, int upperCategoryCount) {
        // 상위 카테고리 - 1~999까지
        if(upperCategoryCount < 10) {
            return upprCatCd.append("00").append(upperCategoryCount).append("000").toString();
        }
        if(upperCategoryCount < 100) {
            return upprCatCd.append('0').append(upperCategoryCount).append("000").toString();
        }
        return upprCatCd.append(upperCategoryCount).append("000").toString();
    }

    public List<CategoryResponseDto> toCategoryResponseDtoList(List<Category> categoryList) {
        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();
        for(Category category : categoryList) {
            categoryResponseDtoList.add(new CategoryResponseDto(category));
        }
        return categoryResponseDtoList;
    }
}
