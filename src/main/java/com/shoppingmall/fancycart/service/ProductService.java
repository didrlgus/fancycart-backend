package com.shoppingmall.fancycart.service;

import com.shoppingmall.fancycart.domain.buyer.Buyer;
import com.shoppingmall.fancycart.domain.buyer.BuyerRepository;
import com.shoppingmall.fancycart.domain.category.Category;
import com.shoppingmall.fancycart.domain.product.Product;
import com.shoppingmall.fancycart.domain.product.ProductRepository;
import com.shoppingmall.fancycart.domain.review.Review;
import com.shoppingmall.fancycart.domain.review.ReviewRepository;
import com.shoppingmall.fancycart.domain.user.User;
import com.shoppingmall.fancycart.domain.user.UserRepository;
import com.shoppingmall.fancycart.exception.ReviewAuthorityException;
import com.shoppingmall.fancycart.exception.InValidCategoryException;
import com.shoppingmall.fancycart.web.dto.ProductRequestDto;
import com.shoppingmall.fancycart.web.dto.ProductResponseDto;
import com.shoppingmall.fancycart.web.dto.ReviewRequestDto;
import com.shoppingmall.fancycart.web.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.shoppingmall.fancycart.utils.ExceptionUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private static final int PRODUCT_PAGE_SIZE = 9;
    private static final int REVIEW_PAGE_SIZE = 10;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BuyerRepository buyerRepository;

    public List<ProductResponseDto> getProductList(ProductRequestDto.Get productRequestDto) {
        paramValidCheck(productRequestDto);

        Page<Product> productList = getPagedProductList(productRequestDto);

        return getProductResponseDtoList(productList);
    }

    public void addProduct(ProductRequestDto productRequestDto) {
        productRequestDtoValidCheck(productRequestDto);

        productRepository.save(Product.toProduct(productRequestDto));
    }

    public void updateProduct(Long id, ProductRequestDto productRequestDto) {
        productRequestDtoValidCheck(productRequestDto);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_EXIST_PRODUCT_MESSAGE));

        product.update(productRequestDto);

        productRepository.save(product);
    }

    public ProductResponseDto.Details getProductDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_EXIST_PRODUCT_MESSAGE));

        return product.toProductDetailsResponseDto(product);
    }

    @Transactional
    public void addReview(Long productId, ReviewRequestDto reviewRequestDto) {
        User user = getUserById(reviewRequestDto.getUserId());

        Product product = getProductById(productId);

        List<Buyer> buyerList = buyerRepository.findByUserIdAndProductId(user.getId(), product.getId());

        if(buyerList.size() == 0) {
            throw new ReviewAuthorityException(REVIEW_AUTHORITY_EXCEPTION_MESSAGE);
        }

        Review review = getReview(user, product, reviewRequestDto);

        // 양방향 모두 셋팅
        reviewRepository.save(review);
        product.getReviewList().add(review);

        product.setRateAvg(getProductRateAvg(product));

        // 상품 평점 업데이트
        productRepository.save(product);
    }

    public List<ReviewResponseDto> getReviews(Long productId, ReviewRequestDto.Get reviewRequestDto) {
        int page = reviewRequestDto.getPage() - 1;
        Pageable pageable
                = PageRequest.of(page, REVIEW_PAGE_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));

        Page<Review> reviewList = reviewRepository.findByProductId(productId, pageable);

        return Review.toReviewResponseDtoList(reviewList);
    }

    private int getProductRateAvg(Product product) {
        // 상품 평점 세팅하기
        double reviewRateAvg = reviewRepository.getReviewRateAvg(product);

        // 1점당 20% 이므로 20을 곱함, 평균값 계산의 결과로 나온 소수점은 버림
        return (int) (reviewRateAvg * 20);
    }

    private Review getReview(User user, Product product, ReviewRequestDto reviewRequestDto) {
        return Review.builder()
                .user(user)
                .product(product)
                .reviewRequestDto(reviewRequestDto)
                .build();
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException(NO_EXIST_USER_MESSAGE));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException(NO_EXIST_PRODUCT_MESSAGE));
    }


    private void productRequestDtoValidCheck(ProductRequestDto productRequestDto) {
        try {
            Category.upperCategoryCdValidCheck(productRequestDto.getLargeCatCd());
            Category.lowerCategoryCdValidCheck(productRequestDto.getSmallCatCd());
        } catch (InValidCategoryException e1) {
            log.error("#### ProductService.productRequestDtoValidCheck {}", e1.getMessage());
            throw new InValidCategoryException(INVALID_CATEGORY_CODE_MESSAGE);
        }
    }
    
    private void paramValidCheck(ProductRequestDto.Get productRequestDto) {
        try {
            Category.lowerCategoryCdValidCheck(productRequestDto.getCategoryCd());
        } catch (Exception exception) {
            log.error("#### ProductService.paramValidCheck {}", exception.getMessage());
            throw new InValidCategoryException(INVALID_CATEGORY_CODE_MESSAGE);
        }
    }

    private Page<Product> getPagedProductList(ProductRequestDto.Get productRequestDto) {
        int page = productRequestDto.getPage() - 1;
        Pageable pageable
                = PageRequest.of(page, PRODUCT_PAGE_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));

        String categoryCd = productRequestDto.getCategoryCd();
        Page<Product> productList = null;
        if(categoryCd == null) {
            productList = productRepository.findAll(pageable);
        }
        if(categoryCd != null) {
            productList = productRepository.findBySmallCatCd(categoryCd, pageable);
        }

        return productList;
    }

    private List<ProductResponseDto> getProductResponseDtoList(Page<Product> productList) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for(Product product : productList) {
            productResponseDtoList.add(new ProductResponseDto(product));
        }

        return productResponseDtoList;
    }


}
