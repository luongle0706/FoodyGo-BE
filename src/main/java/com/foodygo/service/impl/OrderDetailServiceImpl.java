package com.foodygo.service.impl;

import com.foodygo.dto.response.OrderDetailResponse;
import com.foodygo.entity.OrderDetail;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.OrderDetailMapper;
import com.foodygo.repository.OrderDetailRepository;
import com.foodygo.service.spec.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetailResponse getOrderDetailById(Integer id) {
        return OrderDetailMapper.INSTANCE.toDto(orderDetailRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("OrderDetail not found")));
    }

    @Override
    public Page<OrderDetailResponse> getOrderDetailsByOrderId(Integer orderId, Pageable pageable) {
        Page<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId, pageable);
        return orderDetails.map(orderDetail -> new OrderDetailResponse(
                orderDetail.getId(),
                orderDetail.getOrder().getId(),
                orderDetail.getQuantity(),
                orderDetail.getPrice(),
                orderDetail.getAddonItems(),
                orderDetail.getProduct().getName()
        ));
    }

    @Override
    public void deleteOrderDetailsByOrderId(Integer orderId) {
        orderDetailRepository.deleteByOrderId(orderId);
    }
}
