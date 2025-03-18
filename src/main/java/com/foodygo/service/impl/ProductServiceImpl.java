package com.foodygo.service.impl;

import com.foodygo.dto.product.ProductDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.paging.ProductPagingResponse;
import com.foodygo.dto.product.ProductUpdateRequest;
import com.foodygo.dto.product.ProductCreateRequest;
import com.foodygo.entity.AddonSection;
import com.foodygo.entity.Category;
import com.foodygo.entity.Product;
import com.foodygo.entity.Restaurant;
import com.foodygo.exception.BadRequestException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.ProductMapper;
import com.foodygo.repository.AddonSectionRepository;
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
    private final AddonSectionRepository addonSectionRepository;

    @Override
    @Transactional
    public void linkAddonSection(Integer addonSectionId, Integer productId) {
        AddonSection addonSection = addonSectionService.getAddonSectionById(addonSectionId);
        if (addonSection == null) {
            throw new BadRequestException("Unable to find addon section with id " + addonSectionId);
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ElementNotFoundException("Unable to find product with id " + productId)
        );

        // Create a new mutable list to avoid UnsupportedOperationException
        List<Product> linkedProducts = new ArrayList<>(addonSection.getProducts());

        boolean exists = linkedProducts.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));

        if (exists) {
            // Remove the product if it already exists
            linkedProducts.removeIf(p -> p.getId().equals(productId));
        } else {
            // Add the product if it doesn't exist
            linkedProducts.add(product);
        }

        // Set the new list of products
        addonSection.setProducts(linkedProducts);

        // Save the updated addon section
        addonSectionRepository.save(addonSection);
    }

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
    public void updateProductInfo(MultipartFile image, Integer productId, ProductUpdateRequest request) {


        Product product = getProductById(productId);
        if (product == null) {
            throw new BadRequestException("Unable to find product with id " + productId);
        }
        String urlImage = s3Service.uploadFileToS3(image, "productImage");
        if (urlImage != null) product.setImage(urlImage);
        if (request.getCode() != null) product.setCode(request.getCode());
        if (request.getName() != null) product.setName(request.getName());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrepareTime() != null) product.setPrepareTime(request.getPrepareTime());
        if (request.getAvailable() != null) product.setAvailable(request.getAvailable());
        if (request.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(request.getCategoryId());
            if (category != null) product.setCategory(category);
        }
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