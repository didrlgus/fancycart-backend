package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.user.Role;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.validator.ValidCustomException;
import com.shoppingmall.fancycart.web.dto.UserRequestDto;
import com.shoppingmall.fancycart.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.shoppingmall.fancycart.utils.ExceptionUtils.DUPLICATED_EMAIL_MASSAGE;
import static com.shoppingmall.fancycart.utils.ExceptionUtils.NO_EXIST_USER_MESSAGE;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(UserRequestDto.Post userRequestDto) {
        // 이메일 유효성 검사
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ValidCustomException(DUPLICATED_EMAIL_MASSAGE, "email");
        }

        userRepository.save(User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .role(Role.USER)
                .build());
    }

    public Long getAuthUserId(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException(NO_EXIST_USER_MESSAGE));

        return user.getId();
    }

    public UserResponseDto.Profile getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_EXIST_USER_MESSAGE));

        return user.toProfileResponseDto(user);
    }
}
