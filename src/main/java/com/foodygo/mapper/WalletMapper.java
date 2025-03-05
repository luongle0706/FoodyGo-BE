package com.foodygo.mapper;


import com.foodygo.dto.response.WalletBalanceResponse;
import com.foodygo.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    WalletBalanceResponse toDTO(Wallet wallet);

}
