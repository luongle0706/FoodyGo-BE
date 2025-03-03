package com.foodygo.service.spec;

import com.foodygo.dto.ProductDTO;
import com.foodygo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product getProductById(Integer productId);
    ProductDTO getProductDTOById(Integer productId);

    List<Product> getAllProducts();
    Page<ProductDTO> getAllProductDTOs(Pageable pageable);

    List<Product> getAllProductsByRestaurantId(Integer restaurantId);
    Page<ProductDTO> getAllProductDTOsByRestaurantId(Integer restaurantId, Pageable pageable);

    List<Product> getAllProductsByCategoryId(Integer categoryId);
    Page<ProductDTO> getAllProductDTOsByCategoryId(Integer categoryId, Pageable pageable);

    void updateProductInfo(ProductDTO productDTO);

    void createProduct(ProductDTO productDTO);

    void deleteProduct(Integer productId);

    boolean switchProductAvailability(Integer productId);

    public List<Product> getProductsByIds(List<Integer> productIds);
}