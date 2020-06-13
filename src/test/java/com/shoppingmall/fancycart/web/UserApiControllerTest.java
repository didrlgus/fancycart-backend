package com.shoppingmall.fancycart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.fancycart.auth.AuthUtils;
import com.shoppingmall.fancycart.domain.BaseTimeEntity;
import com.shoppingmall.fancycart.domain.tag.Tag;
import com.shoppingmall.fancycart.domain.tag.TagRepository;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.utils.ExceptionUtils;
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
import org.springframework.test.web.servlet.MvcResult;
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
    private final static ResultMatcher STATUS_OK = status().isOk();
    private final static ResultMatcher STATUS_CLIENT_ERROR = status().is4xxClientError();
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

        MvcResult result = callGetProfileAPI(user, STATUS_OK);

        assertEquals(result.getResponse().getContentAsString(),
                new ObjectMapper().writeValueAsString(new UserProfileResponseDto(user)));
    }

    // 존재하지 않는 유저 조회 요청 테스트
    @Test
    public void getProfileOfNotExistUserTest() throws Exception {
        User notExistUser = User.builder().id(0L).build();
        MvcResult result = callGetProfileAPI(notExistUser, STATUS_CLIENT_ERROR);

        String exceptionMessage = result.getResponse().getContentAsString();
        assertEquals(exceptionMessage, ExceptionUtils.NO_EXIST_USER_MESSAGE);
    }

    // 프로필 업데이트 테스트
    @Test
    public void updateProfile() throws Exception {
        User user = authUtils.getAuthenticatedUser();

        MvcResult result = callUpdateProfileAPI(user, STATUS_OK);
        assertEquals(result.getResponse().getContentAsString(), String.valueOf(user.getId()));

        User modifiedUser = authUtils.getAuthenticatedUser();

        assertEquals(modifiedUser.getRoadAddr(), ROAD_ADDR);
        assertEquals(modifiedUser.getBuildingName(), BUILDING_NAME);
        assertEquals(modifiedUser.getDetailAddr(), DETAIL_ADDR);
    }

    // 프로필 업데이트 유효성 테스트
    @Test
    public void updateProfileValidCheck() throws Exception {
        User user = authUtils.getAuthenticatedUser();

        MvcResult result = callUpdateProfileAPI(user, STATUS_CLIENT_ERROR);

        String exceptionMessage = result.getResponse().getContentAsString();
        assertEquals(exceptionMessage, ExceptionUtils.INPUT_EXCEPTION_MESSAGE);
    }

    // 로그아웃 테스트
    @Test
    public void logoutTest() throws Exception {
        callLogoutAPI();
    }

    // 관심 태그 조회 테스트
    @Test
    public void getTagTest() throws Exception {
        User user = authUtils.getAuthenticatedUser();

        callGetTagAPI(user);
    }

    // 관심 태그 추가 테스트
    @Test
    public void addTagTest() throws Exception {
        TagRequestDto tagRequestDto = getNewTagRequestDto();
        tagRepository.save(getTagFromTagRequestDto(tagRequestDto));

        User user = authUtils.getAuthenticatedUser();

        callAddTagAPI(user, tagRequestDto, STATUS_OK);

        user = authUtils.getAuthenticatedUser();
        Tag tag = getAddedTagFromUser(user);

        assertEquals(tagRequestDto.getTitle(), tag.getTitle());
    }

    // 태그 중복 추가 유효성 테스트
    @Test
    public void addTagDuplicatedTest() throws Exception {
        TagRequestDto tagRequestDto = getNewTagRequestDto();
        tagRepository.save(getTagFromTagRequestDto(tagRequestDto));

        User user = authUtils.getAuthenticatedUser();

        callAddTagAPI(user, tagRequestDto, STATUS_OK);
        MvcResult result = callAddTagAPI(user, tagRequestDto, STATUS_CLIENT_ERROR);

        assertEquals(result.getResponse().getContentAsString(), ExceptionUtils.DUPLICATED_TAG_MESSAGE);
    }

    // 태그 10개 초과 시 유효성 테스트
    @Test
    public void addTagExceedTest() throws Exception {
        User user = authUtils.getAuthenticatedUser();
        MvcResult result = null;
        for(int i = 0; i <= 10; i++) {
            TagRequestDto tagRequestDto = getRandomTagRequestDto();
            tagRepository.save(getTagFromTagRequestDto(tagRequestDto));

            ResultMatcher status = i >= 10 ? STATUS_CLIENT_ERROR : STATUS_OK;
            result = callAddTagAPI(user, tagRequestDto, status);
        }
        assertEquals(result.getResponse().getContentAsString(), ExceptionUtils.AVAILABLE_TAG_EXCEED_MESSAGE);
    }

    // 유저가 추가한 태그 삭제 테스트
    @Test
    public void deleteTagTest() throws Exception {
        TagRequestDto tagRequestDto = getNewTagRequestDto();
        tagRepository.save(getTagFromTagRequestDto(tagRequestDto));

        User user = authUtils.getAuthenticatedUser();
        int size = user.getTags().size();

        callAddTagAPI(user, tagRequestDto, STATUS_OK);

        user = authUtils.getAuthenticatedUser();
        Tag tag = getAddedTagFromUser(user);

        callDeleteTagAPI(user, tag, STATUS_OK);

        user = authUtils.getAuthenticatedUser();

        assertEquals(user.getTags().size(), size);
    }

    private MvcResult callGetProfileAPI(User user, ResultMatcher status) throws Exception {
        return mockMvc.perform(get(API_VERSION + "/profiles/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult callUpdateProfileAPI(User user, ResultMatcher status) throws Exception {
        return mockMvc.perform(put(API_VERSION + "/profiles/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(getUserProfileRequestDto(status))))
                .andExpect(status)
                .andReturn();
    }

    private void callLogoutAPI() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

    private void callGetTagAPI(User user) throws Exception {
        mockMvc.perform(get(API_VERSION + "/user/" + user.getId() + "/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(user.getTags())));
    }

    private MvcResult callAddTagAPI(User user, TagRequestDto tagRequestDto, ResultMatcher status) throws Exception {
        return mockMvc.perform(post(API_VERSION + "/user/" + user.getId() + "/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(tagRequestDto)))
                .andExpect(status).andReturn();
    }

    private void callDeleteTagAPI(User user, Tag tag, ResultMatcher status) throws Exception {
        mockMvc.perform(delete(API_VERSION + "/user/" + user.getId() + "/tags/" + tag.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status);
    }

    private UserProfileRequestDto getUserProfileRequestDto(ResultMatcher status) {
        return status.equals(STATUS_OK) ? getValidUserProfileRequestDto() : getUnValidUserProfileRequestDto();
    }

    private UserProfileRequestDto getValidUserProfileRequestDto() {
        return new UserProfileRequestDto(ROAD_ADDR, BUILDING_NAME, DETAIL_ADDR, AGREE_MESSAGE_BY_EMAIL);
    }

    private UserProfileRequestDto getUnValidUserProfileRequestDto() {
        return new UserProfileRequestDto("", BUILDING_NAME, DETAIL_ADDR, AGREE_MESSAGE_BY_EMAIL);
    }

    private TagRequestDto getNewTagRequestDto() {
        return TagRequestDto.builder().title("newTag").build();
    }

    private Tag getTagFromTagRequestDto(TagRequestDto tagRequestDto) {
        return Tag.builder().title(tagRequestDto.getTitle()).build();
    }

    private Tag getAddedTagFromUser(User user) {
        return user.getTags().stream()
                .max(Comparator.comparing(BaseTimeEntity::getCreatedDate))
                .orElseThrow(NoSuchElementException::new);
    }

    private TagRequestDto getRandomTagRequestDto() {
        UUID uuid = UUID.randomUUID();
        String randomTagTitle = uuid.toString();
        return TagRequestDto.builder().title(randomTagTitle).build();
    }
}
