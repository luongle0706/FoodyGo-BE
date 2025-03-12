package com.foodygo.service.impl;

import com.foodygo.dto.response.OrderActivityResponse;
import com.foodygo.entity.Order;
import com.foodygo.entity.OrderActivity;
import com.foodygo.entity.User;
import com.foodygo.enums.OrderStatus;
import com.foodygo.mapper.OrderActivityMapper;
import com.foodygo.repository.OrderActivityRepository;
import com.foodygo.repository.OrderRepository;
import com.foodygo.repository.UserRepository;
import com.foodygo.service.spec.OrderActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderActivityServiceImpl implements OrderActivityService {

    private final OrderActivityRepository orderActivityRepository;
    private final OrderActivityMapper orderActivityMapper;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void logOrderStatusChange(Integer orderId, Integer userId, OrderStatus fromStatus, OrderStatus toStatus, String image) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = userRepository.getUserByUserID(userId);

        OrderActivity activity = OrderActivity.builder()
                .order(order)
                .user(user)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .time(LocalDateTime.now())
                .image(image)
                .build();

        orderActivityRepository.save(activity);
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
