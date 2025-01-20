package com.foodygo.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T entity);
}