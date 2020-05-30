package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.exception.ExceptionUtils;
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
        return new UserProfileResponseDto(getUser(id));
    }

    public Long updateProfile(Long id, UserProfileRequestDto userProfileRequestDto) {
        User user = getUser(id);
        user = updateUserAddr(user, userProfileRequestDto);
        return user.getId();
    }

    private User getUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElseThrow(()-> new NoSuchElementException(ExceptionUtils.NO_EXIST_USER_MESSAGE));
    }

    private User updateUserAddr(User user, UserProfileRequestDto userProfileRequestDto) {
        User modifiedUser = user.update(userProfileRequestDto.getRoadAddr(),
                userProfileRequestDto.getBuildingName(), userProfileRequestDto.getDetailAddr(),
                userProfileRequestDto.isAgreeMessageByEmail());
        return userRepository.save(modifiedUser);
    }
}
