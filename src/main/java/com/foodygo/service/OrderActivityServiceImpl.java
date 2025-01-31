package com.foodygo.service;

import com.foodygo.dto.request.OrderActivityCreateRequest;
import com.foodygo.dto.response.OrderActivityResponse;
import com.foodygo.entity.OrderActivity;
import com.foodygo.mapper.OrderActivityMapper;
import com.foodygo.repository.OrderActivityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<OrderActivityResponse> getOrderActivitiesByOrderId(Integer orderId) {
        List<OrderActivity> activities = orderActivityRepository.findByOrderIdOrderByTimeDesc(orderId);
        return activities.stream()
                .map(orderActivityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllOrderActivitiesByOrderId(List<OrderActivity> orderActivities) {
        orderActivityRepository.deleteAll(orderActivities);
    }
}
