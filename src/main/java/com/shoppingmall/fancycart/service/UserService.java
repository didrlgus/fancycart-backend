package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponseDto getProfile(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt.orElseThrow(NoSuchElementException::new);

        return new UserProfileResponseDto(user);
    }

    public Long updateProfile(Long id, UserProfileRequestDto userProfileRequestDto) {
        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt.orElseThrow(NoSuchElementException::new);

        user.update(userProfileRequestDto.getRoadAddr(),
                userProfileRequestDto.getBuildingName(), userProfileRequestDto.getDetailAddr());

        userRepository.save(user);
        return user.getId();
    }
}
