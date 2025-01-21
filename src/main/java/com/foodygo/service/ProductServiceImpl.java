package com.foodygo.service;

import com.foodygo.dto.ProductDTO;
import com.foodygo.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements  ProductService {
    @Override
    public Product getProductById(Integer productId) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public List<Product> getAllProductsPagination(Integer page, Integer items) {
        return null;
    }

    @Override
    public List<Product> getAllProductsByRestaurantId(Integer restaurantId) {
        return null;
    }

    @Override
    public List<Product> getAllProductsByRestaurantIdPagination(Integer restaurantId, Integer page, Integer items) {
        return null;
    }

    @Override
    public List<Product> getAllProductsByCategoryId(Integer categoryId) {
        return null;
    }

    @Override
    public List<Product> getAllProductsByCategoryIdPagination(Integer categoryId, Integer page, Integer items) {
        return null;
    }

    @Override
    public void updateProductInfo(ProductDTO productDTO) {

    }

    @Override
    public void createProduct(ProductDTO productDTO) {

    }

    @Override
    public void deleteProduct(Integer product) {

    }
}
