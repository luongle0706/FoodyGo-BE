package com.foodygo.service;

import com.foodygo.dto.ProductDTO;
import com.foodygo.entity.Product;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.ProductMapper;
import com.foodygo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements  ProductService {

    private final ProductRepository productRepository;

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
    public List<Product> getAllProducts() {
        return productRepository.findByDeletedFalse();
    }

    @Override
    public Page<ProductDTO> getAllProductDTOs(Pageable pageable) {
        return productRepository.findByDeletedFalse(pageable).map(ProductMapper.INSTANCE::toDTO);
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
    public void createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .code(productDTO.code())
                .name(productDTO.name())
                .price(productDTO.price())
                .description(productDTO.description())
                .prepareTime(productDTO.prepareTime())
                .build();
        productRepository.save(product);
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
    }

    @Override
    public void deleteProduct(Integer productId) {
        Product product = getProductById(productId);
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public boolean switchProductAvailability(Integer productId) {
        Product product = getProductById(productId);
        product.setAvailable(!product.isAvailable());
        productRepository.save(product);
        return product.isAvailable();
    }
}
