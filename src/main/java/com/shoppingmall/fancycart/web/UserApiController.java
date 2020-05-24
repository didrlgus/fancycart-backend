package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.service.UserService;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/api/v1/profiles/{id}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    // TODO. requestdto 유효성 검사
    @PutMapping("/api/v1/profiles/{id}")
    public ResponseEntity<Long> updateProfile(@PathVariable Long id,
                                              @RequestBody UserProfileRequestDto userProfileRequestDto) {
        return ResponseEntity.ok(userService.updateProfile(id,userProfileRequestDto));
    }
}
