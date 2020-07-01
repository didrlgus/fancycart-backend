package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.service.UserService;
import com.shoppingmall.fancycart.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;
import static com.shoppingmall.fancycart.utils.RequestSuccessUtils.ADD_USER_SUCCESS_MESSAGE;

@RequiredArgsConstructor
@RequestMapping(API_VERSION)
@RestController
public class UserApiController {

    private final UserService userService;

    // 유저 생성
    @PostMapping("/user")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequestDto.Post userRequestDto) {

        userService.addUser(userRequestDto);

        return ResponseEntity.ok(ADD_USER_SUCCESS_MESSAGE);
    }
}
