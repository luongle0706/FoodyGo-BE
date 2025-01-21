package com.foodygo.service;

import com.foodygo.dto.ProductDTO;
import com.foodygo.entity.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Integer productId);
    List<Product> getAllProducts();
    List<Product> getAllProductsPagination(Integer page, Integer items);
    List<Product> getAllProductsByRestaurantId(Integer restaurantId);
    List<Product> getAllProductsByRestaurantIdPagination(Integer restaurantId, Integer page, Integer items);
    List<Product> getAllProductsByCategoryId(Integer categoryId);
    List<Product> getAllProductsByCategoryIdPagination(Integer categoryId, Integer page, Integer items);
    void updateProductInfo(ProductDTO productDTO);
    void createProduct(ProductDTO productDTO);
    void deleteProduct(Integer product);
}