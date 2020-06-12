package com.shoppingmall.fancycart.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.product.ProductRepository;
import com.shoppingmall.fancycart.utils.ExceptionUtils;
import com.shoppingmall.fancycart.utils.ApiUtils;
import com.shoppingmall.fancycart.utils.RequestSuccessUtils;
import com.shoppingmall.fancycart.web.dto.ProductRequestDto;
import com.shoppingmall.fancycart.web.dto.ProductResponseDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductApiControllerTest {

    private final String API_VERSION = ApiUtils.API_VERSION;
    private final static ResultMatcher STATUS_OK = status().isOk();
    private final static ResultMatcher STATUS_CLIENT_ERROR = status().is4xxClientError();
    private final static int VALID_PAGE = 1;
    private final static int INVALID_PAGE = -1;
    private final static String VALID_UPPER_CATEGORY_CD = "C001000";
    private final static String VALID_LOWER_CATEGORY_CD = "C001001";
    private final static String INVALID_UPPER_CATEGORY_CD = "C000001";
    private final static String INVALID_LOWER_CATEGORY_CD = "C001000";
    private final static String VALID_PRODUCT_NAME = "Test-product";
    private final static String INVALID_PRODUCT_NAME = "";
    private final static Integer VALID_PRICE = 10000;
    private final static Integer INVALID_PRICE = -1;
    private final static Integer VALID_TOTAL_COUNT = 100;
    private final static Integer INVALID_TOTAL_COUNT = 0;
    private final static String VALID_TITLE_IMG = "test.png";
    private final static String INVALID_TITLE_IMG = "";
    private final static String VALID_FULL_DESCRIPTION = "test-full-description";
    private final static String INVALID_FULL_DESCRIPTION = "";

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void after() {
        productRepository.deleteAll();
    }

    // 상품 조회 테스트
    @Test
    public void getProductTest() throws Exception {
        bulkTestProducts();

        Page<Product> productList = getPagedProductList();

        List<ProductResponseDto> productResponseDtoList = getProductResponseDtoList(productList);

        MultiValueMap<String, String> multiValueMap = getParamForFindProductAPI(VALID_PAGE, VALID_LOWER_CATEGORY_CD);

        MvcResult result = callFindProductAPI(multiValueMap, productResponseDtoList, STATUS_OK);

        assertEquals(result.getResponse().getContentAsString(), new ObjectMapper().writeValueAsString(productResponseDtoList));
    }

    // 상품 조회 유효성 테스트
    @Test
    public void getProductValidTest() throws Exception {
        bulkTestProducts();

        Page<Product> productList = getPagedProductList();

        List<ProductResponseDto> productResponseDtoList = getProductResponseDtoList(productList);

        MultiValueMap<String, String> multiValueMap
                = getParamForFindProductAPI(INVALID_PAGE, VALID_LOWER_CATEGORY_CD);
        // 유효하지 않은 페이지 파라미터 포함 요청 시
        MvcResult result = callFindProductAPI(multiValueMap, productResponseDtoList, STATUS_CLIENT_ERROR);
        assertEquals(result.getResponse().getContentAsString(), ExceptionUtils.INPUT_EXCEPTION_MESSAGE);

        multiValueMap = getParamForFindProductAPI(VALID_PAGE, INVALID_LOWER_CATEGORY_CD);

        // 유효하지 않은 카테고리 코드 파라미터 포함 요청 시
        result = callFindProductAPI(multiValueMap, productResponseDtoList, STATUS_CLIENT_ERROR);
        assertEquals(result.getResponse().getContentAsString(), ExceptionUtils.INVALID_CATEGORY_CODE_MESSAGE);
    }

    // 상품 추가 권한 테스트
    @WithMockUser(roles = "USER")
    @Test
    public void addProductAuthorityTest() throws Exception {
        ProductRequestDto productRequestDto = getProductRequestDto();

        callAddProductAPI(productRequestDto, STATUS_CLIENT_ERROR);
    }

    // 상품 추가 테스트
    @WithMockUser(roles = "ADMIN")
    @Test
    public void addProductTest() throws Exception {
        ProductRequestDto productRequestDto = getProductRequestDto();

        MvcResult result = callAddProductAPI(productRequestDto, STATUS_OK);

        Product product = productRepository.findByProductNm("Test-product");

        assertEquals(result.getResponse().getContentAsString(), RequestSuccessUtils.ADD_PRODUCT_SUCCESS_MESSAGE);
        assertEquals(product.getProductNm(), "Test-product");
    }

    // 상품 추가 유효성 테스트 (상품명, 상품 가격, 상품 재고 갯수, 상품 이미지, 상품 상세)
    @WithMockUser(roles = "ADMIN")
    @Test
    public void addProductNameValidTest() throws Exception {
        ProductRequestDto productRequestDto
                = getInvalidProductRequestDto(INVALID_PRODUCT_NAME, VALID_UPPER_CATEGORY_CD, VALID_LOWER_CATEGORY_CD,
                INVALID_PRICE, INVALID_TOTAL_COUNT, INVALID_TITLE_IMG, INVALID_FULL_DESCRIPTION);
        MvcResult result = callAddProductAPI(productRequestDto, STATUS_CLIENT_ERROR);
        assertEquals(result.getResponse().getContentAsString(), ExceptionUtils.INPUT_EXCEPTION_MESSAGE);
    }

    // 상품 추가 유효성 테스트 (카테고리 코드)
    @WithMockUser(roles = "ADMIN")
    @Test
    public void addProductCategoryValidTest() throws Exception {
        ProductRequestDto productRequestDto = getInvalidProductRequestDto(VALID_PRODUCT_NAME, INVALID_UPPER_CATEGORY_CD, VALID_LOWER_CATEGORY_CD,
                VALID_PRICE, VALID_TOTAL_COUNT, VALID_TITLE_IMG, VALID_FULL_DESCRIPTION);
        MvcResult result = callAddProductAPI(productRequestDto, STATUS_CLIENT_ERROR);
        assertEquals(result.getResponse().getContentAsString(), ExceptionUtils.INVALID_CATEGORY_CODE_MESSAGE);

        productRequestDto = getInvalidProductRequestDto(VALID_PRODUCT_NAME, VALID_UPPER_CATEGORY_CD, INVALID_LOWER_CATEGORY_CD,
                VALID_PRICE, VALID_TOTAL_COUNT, VALID_TITLE_IMG, VALID_FULL_DESCRIPTION);
        result = callAddProductAPI(productRequestDto, STATUS_CLIENT_ERROR);
        assertEquals(result.getResponse().getContentAsString(), ExceptionUtils.INVALID_CATEGORY_CODE_MESSAGE);
    }

    private MvcResult callFindProductAPI(MultiValueMap<String, String> param,
                                         List<ProductResponseDto> productResponseDtoList,
                                         ResultMatcher status) throws Exception {
        return mockMvc.perform(get(API_VERSION + "/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .params(param))
                .andExpect(status)
                .andReturn();
    }

    private MvcResult callAddProductAPI(ProductRequestDto productRequestDto,
                                        ResultMatcher status) throws Exception {
        return mockMvc.perform(post(API_VERSION + "/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(productRequestDto)))
                .andExpect(status).andReturn();
    }

    private Page<Product> getPagedProductList() {
        Pageable pageable = PageRequest.of(0, 9, new Sort(Sort.Direction.DESC, "createdDate"));

        return productRepository.findBySmallCatCd(VALID_LOWER_CATEGORY_CD, pageable);
    }

    private List<ProductResponseDto> getProductResponseDtoList(Page<Product> productList) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for(Product product : productList) {
            productResponseDtoList.add(new ProductResponseDto(product));
        }

        return productResponseDtoList;
    }

    private MultiValueMap<String, String> getParamForFindProductAPI(int page, String categoryCd) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("page", String.valueOf(page));
        multiValueMap.add("categoryCd", categoryCd);

        return multiValueMap;
    }

    private void bulkTestProducts() {
        List<Product> productList = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            productList.add(Product.builder()
                    .largeCatCd(VALID_UPPER_CATEGORY_CD)
                    .smallCatCd(VALID_LOWER_CATEGORY_CD)
                    .productNm(VALID_PRODUCT_NAME + (i + 1))
                    .price(VALID_PRICE)
                    .totalCount(VALID_TOTAL_COUNT)
                    .build());
        }
        productRepository.saveAll(productList);
    }

    private ProductRequestDto getProductRequestDto() {
        return ProductRequestDto.builder()
                .productNm(VALID_PRODUCT_NAME)
                .largeCatCd(VALID_UPPER_CATEGORY_CD)
                .smallCatCd(VALID_LOWER_CATEGORY_CD)
                .price(VALID_PRICE)
                .titleImg(VALID_TITLE_IMG)
                .fullDescription(VALID_FULL_DESCRIPTION)
                .totalCount(VALID_TOTAL_COUNT)
                .build();
    }

    private ProductRequestDto getInvalidProductRequestDto(String productNm, String largeCatCd, String smallCatCd,
                                                          Integer price, Integer totalCount, String titleImg,
                                                          String fullDescription) {
        return ProductRequestDto.builder()
                .productNm(productNm)
                .largeCatCd(largeCatCd)
                .smallCatCd(smallCatCd)
                .price(price)
                .totalCount(totalCount)
                .titleImg(titleImg)
                .fullDescription(fullDescription)
                .build();
    }
}
