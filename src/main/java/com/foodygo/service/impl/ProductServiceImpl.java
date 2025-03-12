package com.foodygo.service.impl;

import com.foodygo.dto.ProductDTO;
import com.foodygo.dto.request.ProductCreateRequest;
import com.foodygo.entity.AddonSection;
import com.foodygo.entity.Category;
import com.foodygo.entity.Product;
import com.foodygo.entity.Restaurant;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.ProductMapper;
import com.foodygo.repository.ProductRepository;
import com.foodygo.service.spec.AddonSectionService;
import com.foodygo.service.spec.CategoryService;
import com.foodygo.service.spec.ProductService;
import com.foodygo.service.spec.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Product> getAllProducts() {return productRepository.findByDeletedFalse();}

    private String getKeyFrom(Pageable pageRequest){
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("id"))
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";
        return String.format(KEY_PRODUCT + ":%d:%d:%s", pageNumber, pageSize, sortDirection);
    }

    @Override
    public Page<ProductDTO> getAllProductDTOs(Pageable pageable) {
        String key = getKeyFrom(pageable) ;
        List<ProductDTO> productDTOS = (List<ProductDTO>) redisTemplate.opsForValue().get(key);
        if (productDTOS != null) {
            return new PageImpl<>(productDTOS, pageable, productDTOS.size());
        }
        Page<ProductDTO> productPage = productRepository.findByDeletedFalse(pageable)
                .map(ProductMapper.INSTANCE::toDTO);

        redisTemplate.opsForValue().set(key, productPage.getContent());
        return productPage;
    }

    private void clear() {
        Set<String> keys = redisTemplate.keys(KEY_PRODUCT + ":*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public List<Product> getAllProductsByRestaurantId(Integer restaurantId) {
        return productRepository.findByRestaurantIdAndDeletedFalse(restaurantId);
    }

    @Override
    public Page<ProductDTO> getAllProductDTOsByRestaurantId(Integer restaurantId, Pageable pageable) {
        return productRepository.findByRestaurantIdAndDeletedFalse(restaurantId, pageable).map(ProductMapper.INSTANCE::toDTO);
    }

    @Override
    public List<Product> getAllProductsByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId);
    }

    @Override
    public Page<ProductDTO> getAllProductDTOsByCategoryId(Integer categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable).map(ProductMapper.INSTANCE::toDTO);
    }

    @Override
    public void createProduct(ProductCreateRequest productDTO) {
        Restaurant restaurant = restaurantService.getRestaurantById(productDTO.getRestaurantId());
        Category category = categoryService.getCategoryById(productDTO.getCategoryId());
        AddonSection addonSection = addonSectionService.getAddonSectionById(productDTO.getAddonSectionId());

        Product product = Product.builder()
                .code(productDTO.getCode())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .image(productDTO.getImage())
                .description(productDTO.getDescription())
                .prepareTime(productDTO.getPrepareTime())
                .restaurant(restaurant)
                .category(category)
                .build();
        productRepository.save(product);
        clear();
    }

    @Override
    public void updateProductInfo(ProductDTO productDTO) {
        Product product = getProductById(productDTO.id());
        product.setCode(productDTO.code());
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setDescription(productDTO.description());
        product.setPrepareTime(productDTO.prepareTime());
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
    public List<Product> getProductsByIds(List<Integer> productIds) {
        return productRepository.findAllById(productIds);
    }

}
