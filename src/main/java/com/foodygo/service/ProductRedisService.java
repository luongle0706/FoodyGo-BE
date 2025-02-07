package com.foodygo.service;

import com.foodygo.dto.ProductDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductRedisService {
    void clear();
    List<ProductDTO> getAllProducts(
            String keyword,
            PageRequest pageRequest);
    void saveAllProducts(List<ProductDTO> productDTOS,
                         String keyword,
                         PageRequest pageRequest);
}
