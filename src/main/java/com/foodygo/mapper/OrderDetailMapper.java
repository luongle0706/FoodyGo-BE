package com.foodygo.mapper;

import com.foodygo.dto.request.OrderDetailCreateRequest;
import com.foodygo.dto.request.OrderDetailUpdateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.OrderDetailResponse;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.entity.Order;
import com.foodygo.entity.OrderDetail;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);
    OrderDetail toEntity(OrderDetailCreateRequest orderDetailCreateRequest);
    OrderDetail toEntity(OrderDetailUpdateRequest orderDetailUpdateRequest);
    @Mapping(source = "product.name", target = "productName")
    OrderDetailResponse toDto(OrderDetail orderDetail);
    List<OrderDetailResponse> mapOrderDetails(List<OrderDetail> orderDetails);
    void updateOrderDetailFromDto(OrderDetailUpdateRequest dto, @MappingTarget OrderDetail orderDetail);
}
