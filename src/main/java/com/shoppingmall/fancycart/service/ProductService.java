package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.category.Category;
import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.product.ProductRepository;
import com.shoppingmall.fancycart.exception.ExceptionUtils;
import com.shoppingmall.fancycart.exception.InValidCategoryException;
import com.shoppingmall.fancycart.web.dto.ProductParamRequestDto;
import com.shoppingmall.fancycart.web.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private static final int PRODUCT_PAGE_SIZE = 9;
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProductList(ProductParamRequestDto productRequestDto) {
        validCheck(productRequestDto);

        Page<Product> productList = getPagedProductList(productRequestDto);

        return getProductResponseDtoList(productList);
    }

    private void validCheck(ProductParamRequestDto productRequestDto) {
        try {
            Category.lowerCategoryCdValidCheck(productRequestDto.getCategoryCd());
        } catch (Exception exception) {
            log.error("#### ProductService.getProductList {}", exception.getMessage());
            throw new InValidCategoryException(ExceptionUtils.INVALID_CATEGORY_CODE_MESSAGE);
        }
    }

    private Page<Product> getPagedProductList(ProductParamRequestDto productRequestDto) {
        int page = productRequestDto.getPage() - 1;
        Pageable pageable
                = PageRequest.of(page, PRODUCT_PAGE_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));

        String categoryCd = productRequestDto.getCategoryCd();
        Page<Product> productList = null;
        if(categoryCd == null) {
            productList = productRepository.findAll(pageable);
        }
        if(categoryCd != null) {
            productList = productRepository.findBySmallCatCd(categoryCd, pageable);
        }

        return productList;
    }

    private List<ProductResponseDto> getProductResponseDtoList(Page<Product> productList) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for(Product product : productList) {
            productResponseDtoList.add(new ProductResponseDto(product));
        }

        return productResponseDtoList;
    }
}
