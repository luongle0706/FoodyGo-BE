package com.foodygo.mapper;

import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.OrderDetailResponse;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.entity.Order;
import com.foodygo.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toEntity(OrderCreateRequest dto);

    @Mapping(target = "orderDetails", ignore = true)
    void updateOrderFromDto(OrderUpdateRequest dto, @MappingTarget Order order);

    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "customer.user.fullName", target = "customerName")
    @Mapping(source = "restaurant.name", target = "restaurantName")
    @Mapping(source = "hub.name", target = "hubName")
    OrderResponse toDto(Order order);
}
