package com.foodygo.service;

import com.foodygo.dto.response.PagingResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class BaseServiceImpl<T, ID> implements BaseService <T, ID> {

    private final JpaRepository<T, ID> repository;

    public BaseServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public PagingResponse findAll(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = repository.findAll(pageable);

        return PagingResponse.builder()
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public T findById(ID id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public T save(T entity) {
        return this.repository.save(entity);
    }

}

