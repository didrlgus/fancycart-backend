package com.shoppingmall.fancycart.web;

import com.shoppingmall.fancycart.domain.tag.Tag;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.service.UserService;
import com.shoppingmall.fancycart.web.dto.TagRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.shoppingmall.fancycart.utils.ApiUtils.API_VERSION;
import static com.shoppingmall.fancycart.utils.ExceptionUtils.*;
import static com.shoppingmall.fancycart.utils.RequestSuccessUtils.*;

@RequiredArgsConstructor
@RequestMapping(API_VERSION)
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
            return ResponseEntity.badRequest().body(INPUT_EXCEPTION_MESSAGE);
        }

        return ResponseEntity.ok(userService.updateProfile(id,userProfileRequestDto));
    }

    @GetMapping("/user/{id}/tags")
    public ResponseEntity<Set<Tag>> getTags(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getTags(id));
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getRoadAddr());
        System.out.println(user.getBuildingName());
        System.out.println(user.getDetailAddr());
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("/user/{id}/tags")
    public ResponseEntity<String> addTag(@PathVariable Long id, @RequestBody TagRequestDto tagRequestDto) {

        userService.addTag(id, tagRequestDto);

        return ResponseEntity.ok().body(ADD_TAG_SUCCESS_MESSAGE);
    }

    @DeleteMapping("/user/{userId}/tags/{tagId}")
    public ResponseEntity<String> deleteTag(@PathVariable("userId") Long userId, @PathVariable("tagId") Long tagId) {

        userService.deleteTag(userId, tagId);

        return ResponseEntity.ok().body(DELETE_TAG_SUCCESS_MESSAGE);
    }
}
