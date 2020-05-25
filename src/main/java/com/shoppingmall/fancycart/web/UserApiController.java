package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.service.UserService;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/api/v1/profiles/{id}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/api/v1/profiles/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                              @Valid @RequestBody UserProfileRequestDto userProfileRequestDto,
                                              Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body("입력값이 유효하지 않습니다.");
        }

        return ResponseEntity.ok(userService.updateProfile(id,userProfileRequestDto));
    }
}
