package com.foodygo.service.spec;

import com.foodygo.dto.ProductDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.request.ProductCreateRequest;
import com.foodygo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJacksonValue;

public interface ProductService {
    Product getProductById(Integer productId);

    ProductDTO getProductDTOById(Integer productId);

    Page<ProductDTO> getAllProductDTOsByRestaurantId(Integer restaurantId, Pageable pageable);

    Page<ProductDTO> getAllProductDTOsByCategoryId(Integer categoryId, Pageable pageable);

    void updateProductInfo(ProductDTO productDTO);

    void createProduct(ProductCreateRequest productDTO);

    void deleteProduct(Integer productId);

    boolean switchProductAvailability(Integer productId);

    MappingJacksonValue getProducts(PagingRequest request);
}