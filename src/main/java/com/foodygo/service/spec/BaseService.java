package com.foodygo.service.spec;

import com.foodygo.dto.response.PagingResponse;

public interface BaseService<T, ID> {
    PagingResponse findAll(int currentPage, int pageSize);
    T findById(ID id);
    T save(T entity);
}