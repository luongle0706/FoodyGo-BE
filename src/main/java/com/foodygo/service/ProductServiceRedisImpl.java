package com.foodygo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodygo.configuration.RedisConfig;
import com.foodygo.dto.ProductDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductServiceRedisImpl implements ProductRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private String getKeyFrom(String keyword, PageRequest pageRequest){
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("id"))
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";
        return String.format("all_products:%d:%d:%s", pageNumber, pageSize, sortDirection);
    }

    @Override
    public void clear() {

    }

    @Override
    public List<ProductDTO> getAllProducts(String keyword, PageRequest pageRequest) {
        String key = this.getKeyFrom(keyword, pageRequest);
        List<ProductDTO>  products = (List<ProductDTO>) redisTemplate.opsForValue().get(key);
        return products;
    }

    @Override
    public void saveAllProducts(List<ProductDTO> productDTOS, String keyword, PageRequest pageRequest) {

    }
}
