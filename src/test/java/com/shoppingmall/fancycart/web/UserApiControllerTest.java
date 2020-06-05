package com.shoppingmall.fancycart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.fancycart.auth.AuthUtils;
import com.shoppingmall.fancycart.domain.BaseTimeEntity;
import com.shoppingmall.fancycart.domain.tag.Tag;
import com.shoppingmall.fancycart.domain.tag.TagRepository;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.exception.ExceptionUtils;
import com.shoppingmall.fancycart.utils.ApiUtils;
import com.shoppingmall.fancycart.web.dto.TagRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static com.shoppingmall.fancycart.auth.AuthUtils.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest {

    private final String API_VERSION = ApiUtils.API_VERSION;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AuthUtils authUtils;

    @Before
    public void setup() {
        userRepository.deleteAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        authUtils.authenticate();
    }

    @After
    public void after() {
        userRepository.deleteAll();
        tagRepository.deleteAll();
    }

    // 프로필 조회 테스트
    @Test
    public void getProfile() throws Exception {
        User user = authUtils.getAuthenticatedUser();

        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto(user);

        mockMvc.perform(get(API_VERSION + "/profiles/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userProfileResponseDto)));
    }

    // 프로필 업데이트 테스트
    @Test
    public void updateProfile() throws Exception {
        User user = authUtils.getAuthenticatedUser();

        UserProfileRequestDto userProfileRequestDto
                = new UserProfileRequestDto(ROAD_ADDR, BUILDING_NAME, DETAIL_ADDR, AGREE_MESSAGE_BY_EMAIL);

        mockMvc.perform(put(API_VERSION + "/profiles/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(userProfileRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(user.getId())));

        User modifiedUser = authUtils.getAuthenticatedUser();

        assertEquals(modifiedUser.getRoadAddr(), ROAD_ADDR);
        assertEquals(modifiedUser.getBuildingName(), BUILDING_NAME);
        assertEquals(modifiedUser.getDetailAddr(), DETAIL_ADDR);
    }
    // 프로필 업데이트 유효성 테스트
    @Test
    public void updateProfileValidCheck() throws Exception {
        User user = authUtils.getAuthenticatedUser();
        UserProfileRequestDto userProfileRequestDto
                = new UserProfileRequestDto("", BUILDING_NAME, DETAIL_ADDR, AGREE_MESSAGE_BY_EMAIL);

        mockMvc.perform(put(API_VERSION + "/profiles/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(userProfileRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(ExceptionUtils.INPUT_EXCEPTION_MESSAGE));
    }

    // 존재하지 않는 유저 요청 테스트
    @Test
    public void notExistUserTest() throws Exception {
        mockMvc.perform(get(API_VERSION + "/profiles/" + 0)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(ExceptionUtils.NO_EXIST_USER_MESSAGE));
    }

    // 로그아웃 테스트
    @Test
    public void logoutTest() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

    // 관심 태그 조회 테스트
    @Test
    public void getTagTest() throws Exception {
        User user = authUtils.getAuthenticatedUser();
        Set<Tag> tagSet = user.getTags();

        mockMvc.perform(get(API_VERSION + "/user/" + user.getId() + "/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(tagSet)));
    }

    // 관심 태그 추가 테스트
    @Test
    public void addTagTest() throws Exception {
        TagRequestDto tagRequestDto = TagRequestDto.builder().title("newTag").build();
        tagRepository.save(Tag.builder().title(tagRequestDto.getTitle()).build());

        User user = authUtils.getAuthenticatedUser();

        mockMvc.perform(post(API_VERSION + "/user/" + user.getId() + "/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(tagRequestDto)))
                .andExpect(status().isOk());

        user = authUtils.getAuthenticatedUser();

        Tag tag = user.getTags().stream()
                .max(Comparator.comparing(BaseTimeEntity::getCreatedDate))
                .orElseThrow(NoSuchElementException::new);

        assertEquals(tagRequestDto.getTitle(), tag.getTitle());
    }

    // 태그 중복 추가 유효성 테스트
    @Test
    public void addTagDuplicatedTest() throws Exception {
        TagRequestDto tagRequestDto = TagRequestDto.builder().title("newTag").build();
        tagRepository.save(Tag.builder().title(tagRequestDto.getTitle()).build());

        User user = authUtils.getAuthenticatedUser();

        mockMvc.perform(post(API_VERSION + "/user/" + user.getId() + "/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(tagRequestDto)))
                .andExpect(status().isOk());

        mockMvc.perform(post(API_VERSION + "/user/" + user.getId() + "/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(tagRequestDto)))
                .andExpect(status().is4xxClientError());
    }

    // 태그 10개 초과 시 유효성 테스트
    @Test
    public void addTagExceedTest() throws Exception {
        User user = authUtils.getAuthenticatedUser();
        for(int i=0;i<=10;i++) {
            UUID uuid = UUID.randomUUID();
            String randomTagTitle = uuid.toString();
            TagRequestDto tagRequestDto = TagRequestDto.builder().title(randomTagTitle).build();
            tagRepository.save(Tag.builder().title(tagRequestDto.getTitle()).build());

            ResultMatcher status = i >= 10 ? status().is4xxClientError() : status().isOk();

            mockMvc.perform(post(API_VERSION + "/user/" + user.getId() + "/tags")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(new ObjectMapper().writeValueAsString(tagRequestDto)))
                    .andExpect(status);
        }
    }

    // 유저가 추가한 태그 삭제 테스트
    @Test
    public void deleteTagTest() throws Exception {
        TagRequestDto tagRequestDto = TagRequestDto.builder().title("newTag").build();
        tagRepository.save(Tag.builder().title(tagRequestDto.getTitle()).build());

        User user = authUtils.getAuthenticatedUser();
        int size = user.getTags().size();

        mockMvc.perform(post(API_VERSION + "/user/" + user.getId() + "/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(tagRequestDto)))
                .andExpect(status().isOk());

        user = authUtils.getAuthenticatedUser();

        Tag tag = user.getTags().stream()
                .max(Comparator.comparing(BaseTimeEntity::getCreatedDate))
                .orElseThrow(NoSuchElementException::new);

        mockMvc.perform(delete(API_VERSION + "/user/" + user.getId() + "/tags/" + tag.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        user = authUtils.getAuthenticatedUser();

        assertEquals(user.getTags().size(), size);
    }
}
