package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.exception.ExceptionUtils;
import com.shoppingmall.fancycart.service.ProductService;
import com.shoppingmall.fancycart.utils.ApiUtils;
import com.shoppingmall.fancycart.web.dto.ProductParamRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(ApiUtils.API_VERSION)
@RestController
public class ProductApiController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<?> getProductList(@Valid ProductParamRequestDto productRequestDto, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ExceptionUtils.INPUT_EXCEPTION_MESSAGE);
        }

        return ResponseEntity.ok(productService.getProductList(productRequestDto));
    }
}
