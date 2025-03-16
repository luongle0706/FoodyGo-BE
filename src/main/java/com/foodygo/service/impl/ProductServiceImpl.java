package com.foodygo.service.impl;

import com.foodygo.dto.ProductDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.paging.ProductPagingResponse;
import com.foodygo.dto.request.ProductCreateRequest;
import com.foodygo.entity.AddonSection;
import com.foodygo.entity.Category;
import com.foodygo.entity.Product;
import com.foodygo.entity.Restaurant;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.ProductMapper;
import com.foodygo.repository.ProductRepository;
import com.foodygo.service.spec.*;
import com.foodygo.utils.PaginationUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RestaurantService restaurantService;
    private final CategoryService categoryService;
    private final AddonSectionService addonSectionService;
    private final S3Service s3Service;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String KEY_PRODUCT = "all_products";

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ElementNotFoundException("Product not found with id " + productId));
    }

    @Override
    public ProductDTO getProductDTOById(Integer productId) {
        return ProductMapper.INSTANCE.toDTO(getProductById(productId));
    }

    private String getKeyFrom(Pageable pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("id"))
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";
        return String.format(KEY_PRODUCT + ":%d:%d:%s", pageNumber, pageSize, sortDirection);
    }

    private void clear() {
        Set<String> keys = redisTemplate.keys(KEY_PRODUCT + ":*");
        assert keys != null;
        redisTemplate.delete(keys);
    }

    @Override
    public Page<ProductDTO> getAllProductDTOsByRestaurantId(Integer restaurantId, Pageable pageable) {
        return productRepository.findByRestaurantIdAndDeletedFalse(restaurantId, pageable).map(ProductMapper.INSTANCE::toDTO);
    }

    @Override
    public Page<ProductDTO> getAllProductDTOsByCategoryId(Integer categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable).map(ProductMapper.INSTANCE::toDTO);
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateRequest productDTO, MultipartFile image) {
        Restaurant restaurant = restaurantService.getRestaurantById(productDTO.getRestaurantId());
        Category category = categoryService.getCategoryById(productDTO.getCategoryId());

        String urlImage = s3Service.uploadFileToS3(image, "productImage");
        List<AddonSection> addonSectionList = new ArrayList<>();
        for (Integer addonSectionId : productDTO.getAddonSections()) {
            AddonSection addonSection = addonSectionService.getAddonSectionById(addonSectionId);
            addonSectionList.add(addonSection);
        }

        Product product = Product.builder()
                .code(productDTO.getCode())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .image(urlImage)
                .description(productDTO.getDescription())
                .prepareTime(productDTO.getPrepareTime())
                .restaurant(restaurant)
                .category(category)
                .addonSections(addonSectionList)
                .build();
        productRepository.save(product);
        clear();
    }

    @Override
    @Transactional
    public void updateProductInfo(ProductDTO productDTO) {
        Category category = categoryService.getCategoryById(productDTO.category().id());

        List<AddonSection> addonSectionList = new ArrayList<>();
        for (ProductDTO.AddonSection addonSectionId : productDTO.addonSections()) {
            AddonSection addonSection = addonSectionService.getAddonSectionById(addonSectionId.id());
            addonSectionList.add(addonSection);
        }
        Product product = getProductById(productDTO.id());
        product.setCode(productDTO.code());
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setDescription(productDTO.description());
        product.setPrepareTime(productDTO.prepareTime());
        product.setCategory(category);
        product.setAddonSections(addonSectionList);
        productRepository.save(product);
        clear();
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product product = getProductById(productId);
        product.setDeleted(true);
        productRepository.save(product);
        clear();
    }

    @Override
    public boolean switchProductAvailability(Integer productId) {
        Product product = getProductById(productId);
        product.setAvailable(!product.isAvailable());
        productRepository.save(product);
        clear();
        return product.isAvailable();
    }

    @Override
    public MappingJacksonValue getProducts(PagingRequest request) {
        Pageable pageable = PaginationUtil.getPageable(request);
        Specification<Product> spec = ProductPagingResponse.filterByFields(request.getFilters());
        Page<Product> page = productRepository.findAll(spec, pageable);
        List<ProductPagingResponse> mappedDTOs = page.getContent().stream().map(ProductPagingResponse::fromEntity).toList();
        return PaginationUtil.getPagedMappingJacksonValue(request, page, mappedDTOs, "Get products");
    }

}