package com.foodygo.service;

import com.foodygo.dto.ProductDTO;
import com.foodygo.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product getProductById(Integer productId);
    ProductDTO getProductDTOById(Integer productId);

    List<Product> getAllProducts();
    Page<ProductDTO> getAllProductsPagination(Integer page, Integer size);

    List<Product> getAllProductsByRestaurantId(Integer restaurantId);
    Page<ProductDTO> getAllProductsByRestaurantIdPagination(Integer restaurantId, Integer page, Integer size);

    List<Product> getAllProductsByCategoryId(Integer categoryId);
    Page<ProductDTO> getAllProductsByCategoryIdPagination(Integer categoryId, Integer page, Integer size);

    void updateProductInfo(ProductDTO productDTO);

    void createProduct(ProductDTO productDTO);

    void deleteProduct(Integer productId);

    boolean switchProductAvailability(Integer productId);
}