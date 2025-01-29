package com.foodygo.mapper;


import com.foodygo.entity.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepositMapper {

    DepositMapper INSTANCE = Mappers.getMapper(DepositMapper.class);

}
