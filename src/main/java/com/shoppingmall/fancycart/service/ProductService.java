package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.category.Category;
import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.product.ProductRepository;
import com.shoppingmall.fancycart.utils.ExceptionUtils;
import com.shoppingmall.fancycart.exception.InValidCategoryException;
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
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private static final int PRODUCT_PAGE_SIZE = 9;
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProductList(ProductRequestDto.Get productRequestDto) {
        paramValidCheck(productRequestDto);

        Page<Product> productList = getPagedProductList(productRequestDto);

        return getProductResponseDtoList(productList);
    }

    public void addProduct(ProductRequestDto productRequestDto) {
        productRequestDtoValidCheck(productRequestDto);

        productRepository.save(Product.toProduct(productRequestDto));
    }

    public void updateProduct(Long id, ProductRequestDto productRequestDto) {
        productRequestDtoValidCheck(productRequestDto);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ExceptionUtils.NO_EXIST_PRODUCT_MESSAGE));

        product.update(productRequestDto);

        productRepository.save(product);
    }

    public ProductResponseDto.Details getProductDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ExceptionUtils.NO_EXIST_PRODUCT_MESSAGE));

        return product.toProductDetailsResponseDto(product);
    }

    private void productRequestDtoValidCheck(ProductRequestDto productRequestDto) {
        try {
            Category.upperCategoryCdValidCheck(productRequestDto.getLargeCatCd());
            Category.lowerCategoryCdValidCheck(productRequestDto.getSmallCatCd());
        } catch (InValidCategoryException e1) {
            log.error("#### ProductService.productRequestDtoValidCheck {}", e1.getMessage());
            throw new InValidCategoryException(ExceptionUtils.INVALID_CATEGORY_CODE_MESSAGE);
        }
    }
    
    private void paramValidCheck(ProductRequestDto.Get productRequestDto) {
        try {
            Category.lowerCategoryCdValidCheck(productRequestDto.getCategoryCd());
        } catch (Exception exception) {
            log.error("#### ProductService.paramValidCheck {}", exception.getMessage());
            throw new InValidCategoryException(ExceptionUtils.INVALID_CATEGORY_CODE_MESSAGE);
        }
    }

    private Page<Product> getPagedProductList(ProductRequestDto.Get productRequestDto) {
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
