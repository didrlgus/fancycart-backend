package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.exception.ExceptionUtils;
import com.shoppingmall.fancycart.service.CategoryService;
import com.shoppingmall.fancycart.utils.ApiUtils;
import com.shoppingmall.fancycart.web.dto.CategoryRequestDto;
import com.shoppingmall.fancycart.web.dto.CategoryResponseDto;
import com.shoppingmall.fancycart.web.dto.CategoryUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(ApiUtils.API_VERSION)
@RestController
public class CategoryApiController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponseDto>> getCategory() {
        return ResponseEntity.ok(categoryService.getCategory());
    }

    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto,
                                              Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ExceptionUtils.INPUT_EXCEPTION_MESSAGE);
        }

        categoryService.addCategory(categoryRequestDto);

        return ResponseEntity.ok("카테고리가 생성되었습니다.");
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,
                                                 @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto,
                                                 Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ExceptionUtils.INPUT_EXCEPTION_MESSAGE);
        }

        categoryService.updateCategory(id,categoryUpdateRequestDto);

        return ResponseEntity.ok("카테고리가 변경되었습니다.");
    }
}
