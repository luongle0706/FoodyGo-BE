package com.foodygo.service;

import com.foodygo.dto.request.OrderActivityCreateRequest;
import com.foodygo.dto.response.OrderActivityResponse;
import com.foodygo.entity.OrderActivity;
import com.foodygo.mapper.OrderActivityMapper;
import com.foodygo.repository.OrderActivityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderActivityServiceImpl implements OrderActivityService{

    private final OrderActivityRepository orderActivityRepository;
    private final OrderActivityMapper orderActivityMapper;

    @Override
    @Transactional
    public void logOrderStatusChange(OrderActivityCreateRequest orderActivityCreateRequest) {
        OrderActivity orderActivity = OrderActivityMapper.INSTANCE.toEntity(orderActivityCreateRequest);
        orderActivity.setTime(LocalDateTime.now());
        orderActivityRepository.save(orderActivity);
    }

    @Override
    public Page<OrderActivityResponse> getOrderActivitiesByOrderId(Integer orderId, Pageable pageable) {
        Page<OrderActivity> activities = orderActivityRepository.findByOrderIdOrderByTimeDesc(orderId, pageable);
        return activities.map(orderActivityMapper::toDto);
    }

    @Override
    public void deleteOrderActivitiesByOrderId(Integer orderId) {
        orderActivityRepository.deleteByOrderId(orderId);
    }
}
