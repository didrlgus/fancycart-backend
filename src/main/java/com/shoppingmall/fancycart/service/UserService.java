package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.user.Role;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.validator.ValidCustomException;
import com.shoppingmall.fancycart.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shoppingmall.fancycart.utils.ExceptionUtils.DUPLICATED_EMAIL_MASSAGE;

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
                .agreeMessageByEmail(userRequestDto.getAgreeMessageByEmail())
                .roadAddr(userRequestDto.getRoadAddr())
                .buildingName(userRequestDto.getBuildingName())
                .detailAddr(userRequestDto.getDetailAddr())
                .role(Role.USER)
                .build());
    }
}
