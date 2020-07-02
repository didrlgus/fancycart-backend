package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.service.JwtUtilService;
import com.shoppingmall.fancycart.service.UserService;
import com.shoppingmall.fancycart.validator.ValidCustomException;
import com.shoppingmall.fancycart.web.dto.AuthUser;
import com.shoppingmall.fancycart.web.dto.AuthenticationRequestDto;
import com.shoppingmall.fancycart.web.dto.AuthenticationResponseDto;
import com.shoppingmall.fancycart.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;
import static com.shoppingmall.fancycart.utils.ExceptionUtils.AUTHENTICATION_EXCEPTION_MESSAGE;
import static com.shoppingmall.fancycart.utils.RequestSuccessUtils.ADD_USER_SUCCESS_MESSAGE;

@RequiredArgsConstructor
@RequestMapping(API_VERSION)
@RestController
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtilService jwtUtilService;

    // 유저 생성
    @PostMapping("/user")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequestDto.Post userRequestDto) {

        userService.addUser(userRequestDto);

        return ResponseEntity.ok(ADD_USER_SUCCESS_MESSAGE);
    }

    // 로그인
    @PostMapping("/authentication")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequestDto requestDto) {
        Authentication authentication;
        try {
            // 인증처리
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ValidCustomException(AUTHENTICATION_EXCEPTION_MESSAGE, null);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtilService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponseDto(jwt));
    }

    // 인증된 유저 조회
    @GetMapping("/user")
    public ResponseEntity<?> getAuthUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(AuthUser.builder().email(userDetails.getUsername()).build());
    }
}
