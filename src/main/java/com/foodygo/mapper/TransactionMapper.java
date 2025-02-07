package com.foodygo.mapper;


import com.foodygo.dto.response.TransactionHistoryResponse;
import com.foodygo.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    List<TransactionHistoryResponse> toDTO(List<Transaction> transactions);

    TransactionHistoryResponse toDTO(Transaction transaction);

}
