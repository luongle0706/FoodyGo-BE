package com.foodygo.service.spec;

import com.foodygo.dto.product.ProductDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.product.ProductUpdateRequest;
import com.foodygo.dto.product.ProductCreateRequest;
import com.foodygo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.converter.json.MappingJacksonValue;


public interface ProductService {

    void linkAddonSection(Integer addonSectionId, Integer productId);

    Product getProductById(Integer productId);

    ProductDTO getProductDTOById(Integer productId);

    Page<ProductDTO> getAllProductDTOsByRestaurantId(Integer restaurantId, Pageable pageable);

    Page<ProductDTO> getAllProductDTOsByCategoryId(Integer categoryId, Pageable pageable);

    void updateProductInfo(MultipartFile image, Integer productId, ProductUpdateRequest request);

    void createProduct(ProductCreateRequest productDTO, MultipartFile file);

    void deleteProduct(Integer productId);

    boolean switchProductAvailability(Integer productId);

    MappingJacksonValue getProducts(PagingRequest request);
}