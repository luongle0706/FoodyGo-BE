package com.foodygo.utils;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.foodygo.dto.internal.NewPagingResponse;
import com.foodygo.dto.internal.PagingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

    public static Pageable getPageable(PagingRequest request) {
        String[] sorts = request.getSortBy().split(",");
        List<Sort.Order> sortingOrders = new ArrayList<>();
        for (String sortBy : sorts) {
            String[] parts = sortBy.split(":");
            String fieldName = parts[0];
            Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sortingOrders.add(new Sort.Order(direction, fieldName));
        }
        Sort sort = Sort.by(sortingOrders);
        return request.getPageSize() > 0
                ? PageRequest.of(request.getPageNo() - 1, request.getPageSize(), sort)
                : Pageable.unpaged(sort);
    }

    public static MappingJacksonValue getPagedMappingJacksonValue(PagingRequest request, Page<?> page, List<?> mappedDTO, String message) {
        String[] sorts = request.getSortBy().split(",");
        NewPagingResponse pagingResponse = NewPagingResponse.builder()
                .code(HttpStatus.OK.toString())
                .message(message)
                .currentPage(page.getNumber() + 1)
                .totalElements(page.getTotalElements())
                .pageSize(request.getPageSize())
                .totalPages(page.getTotalPages())
                .sortingOrders(sorts)
                .params(request.getParams() != null ? request.getParams() : "All")
                .filters(request.getFilters())
                .data(mappedDTO)
                .build();
        SimpleFilterProvider filters;
        if (request.getParams() != null && !request.getParams().isBlank()) {
            String[] params = request.getParams().replaceAll(" ", "").split(",");
            filters = new SimpleFilterProvider().addFilter("dynamicFilter", SimpleBeanPropertyFilter.filterOutAllExcept(params));
        } else {
            filters = new SimpleFilterProvider().addFilter("dynamicFilter", SimpleBeanPropertyFilter.serializeAll());
        }
        MappingJacksonValue mapping = new MappingJacksonValue(pagingResponse);
        mapping.setFilters(filters);
        return mapping;
    }

}
