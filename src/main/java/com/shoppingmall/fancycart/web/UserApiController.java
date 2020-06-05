package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.domain.tag.Tag;
import com.shoppingmall.fancycart.exception.ExceptionUtils;
import com.shoppingmall.fancycart.service.UserService;
import com.shoppingmall.fancycart.utils.ApiUtils;
import com.shoppingmall.fancycart.web.dto.TagRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping(ApiUtils.API_VERSION)
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/profiles/{id}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/profiles/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                              @Valid @RequestBody UserProfileRequestDto userProfileRequestDto,
                                              Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ExceptionUtils.INPUT_EXCEPTION_MESSAGE);
        }

        return ResponseEntity.ok(userService.updateProfile(id,userProfileRequestDto));
    }

    @GetMapping("/user/{id}/tags")
    public ResponseEntity<Set<Tag>> getTags(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getTags(id));
    }

    @PostMapping("/user/{id}/tags")
    public ResponseEntity<String> addTag(@PathVariable Long id, @RequestBody TagRequestDto tagRequestDto) {

        userService.addTag(id, tagRequestDto);

        return ResponseEntity.ok().body("태그가 추가 되었습니다.");
    }

    @DeleteMapping("/user/{userId}/tags/{tagId}")
    public ResponseEntity<String> deleteTag(@PathVariable("userId") Long userId, @PathVariable("tagId") Long tagId) {

        userService.deleteTag(userId, tagId);

        return ResponseEntity.ok().body("태그가 삭제 되었습니다.");
    }
}
