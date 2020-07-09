package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.product.ProductRepository;
import com.shoppingmall.fancycart.web.dto.ProductRequestDto;
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
    private static final int PRODUCT_PAGE_SIZE = 8;
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProductList(String category, Integer page) {
        Page<Product> productList = getPagedProductList(category, page);

        return getProductResponseDtoList(productList);
    }

    private Page<Product> getPagedProductList(String category, Integer page) {
        int inputPage = page == null ? 1 : page;
        page = inputPage - 1;
        Pageable pageable
                = PageRequest.of(page, PRODUCT_PAGE_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));

        return productRepository.findByCategory(category, pageable);
    }

    private List<ProductResponseDto> getProductResponseDtoList(Page<Product> productList) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for(Product product : productList) {
            productResponseDtoList.add(new ProductResponseDto(product));
        }

        return productResponseDtoList;
    }
}
