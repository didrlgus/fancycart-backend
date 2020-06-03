package com.shoppingmall.fancycart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.fancycart.auth.AuthUtils;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.exception.ExceptionUtils;
import com.shoppingmall.fancycart.utils.ApiUtils;
import com.shoppingmall.fancycart.web.dto.UserProfileRequestDto;
import com.shoppingmall.fancycart.web.dto.UserProfileResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.shoppingmall.fancycart.auth.AuthUtils.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
}
