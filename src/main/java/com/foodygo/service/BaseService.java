package com.foodygo.service;

import com.foodygo.dto.response.PagingResponse;

import java.util.List;

public interface BaseService<T, ID> {
    PagingResponse findAll(int currentPage, int pageSize);
    T findById(ID id);
    T save(T entity);
}