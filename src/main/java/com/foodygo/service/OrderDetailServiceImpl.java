package com.foodygo.service;

import com.foodygo.dto.request.OrderDetailCreateRequest;
import com.foodygo.dto.request.OrderDetailUpdateRequest;
import com.foodygo.dto.response.OrderDetailResponse;
import com.foodygo.entity.OrderDetail;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.OrderDetailMapper;
import com.foodygo.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail getOrderDetailById(Integer id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("OrderDetail not found"));
    }
}
