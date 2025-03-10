package com.foodygo.mapper;

import com.foodygo.dto.CustomerDTO;
import com.foodygo.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "id", target = "id")
    CustomerDTO customerToCustomerDTO(Customer customer);

    @Mapping(source = "id", target = "id")
    Customer customerDTOToCustomer(CustomerDTO customer);

}
