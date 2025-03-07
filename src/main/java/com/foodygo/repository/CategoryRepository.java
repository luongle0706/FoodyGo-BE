package com.foodygo.repository;

import com.foodygo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByIdAndDeletedFalse(Integer id);

    Page<Category> findByRestaurantIdAndDeletedFalse(Integer restaurantId, Pageable pageable);

    Page<Category> findByDeletedFalse(Pageable pageable);
}
