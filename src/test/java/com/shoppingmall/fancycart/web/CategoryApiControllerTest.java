package com.shoppingmall.fancycart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.fancycart.domain.category.Category;
import com.shoppingmall.fancycart.domain.category.CategoryRepository;
import com.shoppingmall.fancycart.domain.tag.TagRepository;
import com.shoppingmall.fancycart.service.CategoryService;
import com.shoppingmall.fancycart.utils.ApiUtils;
import com.shoppingmall.fancycart.web.dto.CategoryRequestDto;
import com.shoppingmall.fancycart.web.dto.CategoryResponseDto;
import com.shoppingmall.fancycart.web.dto.CategoryUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryApiControllerTest {

    private final String API_VERSION = ApiUtils.API_VERSION;
    private final static int UPPR_CAT_LV = 1;
    private final static int LOWER_CAT_LV = 2;
    private final static ResultMatcher STATUS_OK = status().isOk();
    private final static ResultMatcher STATUS_CLIENT_ERROR = status().is4xxClientError();

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void after() {
        categoryRepository.deleteAll();
        tagRepository.deleteAll();
    }

    // 카테고리 조회 테스트
    @WithMockUser(roles = "USER")
    @Test
    public void getCategoryTest() throws Exception {
        List<Category> categoryList = categoryRepository.findAllByIsAvailable('Y');
        List<CategoryResponseDto> categoryResponseDtoList
                = categoryService.toCategoryResponseDtoList(categoryList);

        callFindCategoryAPI(categoryResponseDtoList, STATUS_OK);
    }

    // 카테고리 추가 권한 테스트
    @WithMockUser(roles = "USER")
    @Test
    public void addCategoryAuthorizationTest() throws Exception {
        CategoryRequestDto categoryRequestDto = getUpprCategoryRequestDto();
        callAddCategoryAPI(categoryRequestDto, STATUS_CLIENT_ERROR);
    }

    // 상위 카테고리 추가 테스트
    @WithMockUser(roles = "ADMIN")
    @Test
    public void addUpprCategoryTest() throws Exception {
        CategoryRequestDto categoryRequestDto = getUpprCategoryRequestDto();

        callAddCategoryAPI(categoryRequestDto, STATUS_OK);

        Category category = getAddedCategory(UPPR_CAT_LV);

        assertEquals(category.getCatLv().intValue(), UPPR_CAT_LV);
        assertEquals(category.getCatCd(), "C001000");
        assertNull(category.getUpprCatCd());
    }

    // 하위 카테고리 추가 테스트
    @WithMockUser(roles = "ADMIN")
    @Test
    public void addLowerCategoryTest() throws Exception {
        CategoryRequestDto categoryRequestDto = getLowerCategoryRequestDto();

        callAddCategoryAPI(categoryRequestDto, STATUS_OK);
        Category category = getAddedCategory(LOWER_CAT_LV);

        assertEquals(category.getCatLv().intValue(), LOWER_CAT_LV);
        assertEquals(category.getCatCd(), "C001001");
        assertNotNull(category.getUpprCatCd());
    }

    // 카테고리 중복 유효성 검사 테스트
    @WithMockUser(roles = "ADMIN")
    @Test
    public void categoryDuplicateTest() throws Exception {
        CategoryRequestDto categoryRequestDto = getLowerCategoryRequestDto();
        callAddCategoryAPI(categoryRequestDto, STATUS_OK);
        callAddCategoryAPI(categoryRequestDto, STATUS_CLIENT_ERROR);
    }

    // 카테고리 업데이트 테스트
    @WithMockUser(roles = "ADMIN")
    @Test
    public void updateCategoryTest() throws Exception {
        CategoryRequestDto categoryRequestDto = getUpprCategoryRequestDto();
        callAddCategoryAPI(categoryRequestDto, STATUS_OK);

        Category addedCategory = getAddedCategory(1);

        CategoryUpdateRequestDto updateRequestDto = getCategoryUpdateRequestDto();
        callUpdateCategoryAPI(addedCategory, updateRequestDto, STATUS_OK);

        Category category = getUpdatedCategory(categoryRequestDto.getCatLv());

        assertEquals(category.getCatNm(), "식품");
        assertEquals(category.getIsAvailable().charValue(), 'N');
    }

    private Category getUpdatedCategory(int catLv) {
        List<Category> categoryList = categoryRepository.findByCatLvOrderByModifiedDateDesc(catLv);

        return categoryList.get(0);
    }

    private CategoryUpdateRequestDto getCategoryUpdateRequestDto() {
        return CategoryUpdateRequestDto.builder()
                .catNm("식품")
                .isAvailable('N')
                .build();
    }

    private Category getAddedCategory(int catLv) {
        List<Category> categoryList = categoryRepository.findByCatLvOrderByCreatedDateDesc(catLv);

        return categoryList.get(0);
    }

    private void callFindCategoryAPI(List<CategoryResponseDto> categoryResponseDtoList, ResultMatcher status) throws Exception {
        mockMvc.perform(get(API_VERSION + "/category")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status)
                .andExpect(content().json(new ObjectMapper().writeValueAsString(categoryResponseDtoList)));
    }

    private void callAddCategoryAPI(CategoryRequestDto categoryRequestDto, ResultMatcher status) throws Exception {
        mockMvc.perform(post(API_VERSION + "/category")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(categoryRequestDto)))
                .andExpect(status);
    }

    private void callUpdateCategoryAPI(Category category, CategoryUpdateRequestDto updateRequestDto,
                                       ResultMatcher status) throws Exception {
        mockMvc.perform(put(API_VERSION + "/category/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(updateRequestDto)))
                .andExpect(status);
    }

    private CategoryRequestDto getLowerCategoryRequestDto() {
        return CategoryRequestDto.builder()
                .catNm("의류")
                .catLv(2)
                .isAvailable('Y')
                .upprCatCd("C001000")
                .build();
    }

    private CategoryRequestDto getUpprCategoryRequestDto() {
        return CategoryRequestDto.builder()
                .catNm("패션")
                .catLv(1)
                .isAvailable('Y')
                .upprCatCd(null)
                .build();
    }
}
