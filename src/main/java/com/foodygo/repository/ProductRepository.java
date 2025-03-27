package com.foodygo.repository;

import com.foodygo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndDeletedFalse(Integer id);

    Page<Product> findByRestaurantIdAndDeletedIsFalseAndAvailableIsTrue(Integer restaurantId, Pageable pageable);

    Page<Product> findByCategoryIdAndDeletedIsFalse(Integer categoryId, Pageable pageable);
}
