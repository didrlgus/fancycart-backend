package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.service.ProductService;
import com.shoppingmall.fancycart.web.dto.ProductRequestDto;
import com.shoppingmall.fancycart.web.dto.ProductResponseDto;
import com.shoppingmall.fancycart.web.dto.ReviewRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;
import static com.shoppingmall.fancycart.utils.ExceptionUtils.*;
import static com.shoppingmall.fancycart.utils.RequestSuccessUtils.*;

@RequiredArgsConstructor
@RequestMapping(API_VERSION)
@RestController
public class ProductApiController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<?> getProductList(@Valid ProductRequestDto.Get productRequestDto, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(INPUT_EXCEPTION_MESSAGE);
        }

        return ResponseEntity.ok(productService.getProductList(productRequestDto));
    }

    @PostMapping("/products")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto,
                                             Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(INPUT_EXCEPTION_MESSAGE);
        }

        productService.addProduct(productRequestDto);

        return ResponseEntity.ok(ADD_PRODUCT_SUCCESS_MESSAGE);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto productRequestDto,
                                                Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(INPUT_EXCEPTION_MESSAGE);
        }

        productService.updateProduct(id, productRequestDto);

        return ResponseEntity.ok(UPDATE_PRODUCT_SUCCESS_MESSAGE);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto.Details> getProductDetails(@PathVariable Long id) {

        return ResponseEntity.ok(productService.getProductDetails(id));
    }

    @PostMapping("/products/{id}/reviews")
    public ResponseEntity<String> addReview(@PathVariable("id") Long productId,
                                                      @Valid @RequestBody ReviewRequestDto reviewRequestDto,
                                                      Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(INPUT_EXCEPTION_MESSAGE);
        }

        productService.addReview(productId, reviewRequestDto);

        return ResponseEntity.ok().body(ADD_REVIEW_SUCCESS_MESSAGE);
    }

}
