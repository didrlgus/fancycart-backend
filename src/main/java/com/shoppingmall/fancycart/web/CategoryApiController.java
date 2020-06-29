package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.service.CategoryService;
import com.shoppingmall.fancycart.web.dto.CategoryRequestDto;
import com.shoppingmall.fancycart.web.dto.CategoryResponseDto;
import com.shoppingmall.fancycart.web.dto.CategoryUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;
import static com.shoppingmall.fancycart.utils.ExceptionUtils.*;
import static com.shoppingmall.fancycart.utils.RequestSuccessUtils.*;

@RequiredArgsConstructor
@RequestMapping(API_VERSION)
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
            return ResponseEntity.badRequest().body(INPUT_EXCEPTION_MESSAGE);
        }

        categoryService.addCategory(categoryRequestDto);

        return ResponseEntity.ok(ADD_CATEGORY_SUCCESS_MESSAGE);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,
                                                 @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto,
                                                 Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(INPUT_EXCEPTION_MESSAGE);
        }

        categoryService.updateCategory(id,categoryUpdateRequestDto);

        return ResponseEntity.ok(UPDATE_CATEGORY_SUCCESS_MESSAGE);
    }
}
