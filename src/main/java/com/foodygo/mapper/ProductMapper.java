package com.foodygo.mapper;

import com.foodygo.dto.ProductDTO;
import com.foodygo.entity.Product;
import com.foodygo.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    @Mapping(source = "image", target = "image")
    @Mapping(source = "restaurant.id", target = "restaurantId")
    ProductDTO toDTO(Product product);
}
