package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.tag.Tag;
import com.shoppingmall.fancycart.domain.tag.TagRepository;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.exception.DuplicatedTagOfUserException;
import com.shoppingmall.fancycart.exception.ExceedTagOfUserException;
import com.shoppingmall.fancycart.web.dto.TagRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static com.shoppingmall.fancycart.utils.ExceptionUtils.*;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public UserProfileResponseDto getProfile(Long id) {
        return new UserProfileResponseDto(getUser(id));
    }

    public Long updateProfile(Long id, UserProfileRequestDto userProfileRequestDto) {
        User user = getUser(id);
        user = updateUserAddr(user, userProfileRequestDto);
        return user.getId();
    }

    public Set<Tag> getTags(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElseThrow(NoSuchElementException::new).getTags();
    }

    public void addTag(Long id, @Valid TagRequestDto tagRequestDto) {
        Tag tag = tagRepository.findByTitle(tagRequestDto.getTitle());

        if(tag == null) {
            throw new NoSuchElementException(NO_EXIST_TAG_MESSAGE);
        }

        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt.orElseThrow(NoSuchElementException::new);

        Set<Tag> tags = user.getTags();

        if(tags.size() >= 10) {
            throw new ExceedTagOfUserException(AVAILABLE_TAG_EXCEED_MESSAGE);
        }

        boolean ret = tags.stream().anyMatch(t -> t.getTitle().equals(tag.getTitle()));

        if(ret) {
            throw new DuplicatedTagOfUserException(DUPLICATED_TAG_MESSAGE);
        }

        userOpt.orElseThrow(NoSuchElementException::new).getTags().add(tag);
    }

    private User getUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElseThrow(()-> new NoSuchElementException(NO_EXIST_USER_MESSAGE));
    }

    private User updateUserAddr(User user, UserProfileRequestDto userProfileRequestDto) {
        User modifiedUser = user.update(userProfileRequestDto.getRoadAddr(),
                userProfileRequestDto.getBuildingName(), userProfileRequestDto.getDetailAddr(),
                userProfileRequestDto.isAgreeMessageByEmail());
        return userRepository.save(modifiedUser);
    }

    public void deleteTag(Long userId, Long tagId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Tag> tagOpt = tagRepository.findById(tagId);

        User user = userOpt.orElseThrow(() -> new NoSuchElementException(NO_EXIST_USER_MESSAGE));
        Tag tag = tagOpt.orElseThrow(() -> new NoSuchElementException(NO_EXIST_TAG_MESSAGE));

        user.getTags().remove(tag);
    }
}
