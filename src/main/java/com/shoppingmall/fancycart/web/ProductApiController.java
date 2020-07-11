package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.service.ProductService;
import com.shoppingmall.fancycart.web.dto.ProductRequestDto;
import com.shoppingmall.fancycart.web.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;

@RequiredArgsConstructor
@RequestMapping(API_VERSION)
@RestController
public class ProductApiController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam String category, @RequestParam(required = false) Integer page) {
        List<ProductResponseDto> result = productService.getProductList(category, page);
        return ResponseEntity.ok().body(result);
    }

}
