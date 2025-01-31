package com.foodygo.mapper;

import com.foodygo.dto.request.OrderActivityCreateRequest;
import com.foodygo.dto.response.OrderActivityResponse;
import com.foodygo.entity.OrderActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderActivityMapper {
    OrderActivityMapper INSTANCE = Mappers.getMapper(OrderActivityMapper.class);
    @Mapping(source = "user.fullName", target = "userName")
    OrderActivityResponse toDto(OrderActivity orderActivity);
    OrderActivity toEntity(OrderActivityCreateRequest orderActivityCreateRequest);
}
