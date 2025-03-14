package com.foodygo.repository;

import com.foodygo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndDeletedFalse(Integer id);

    Optional<Product> findByName(String name);

    List<Product> findByDeletedFalse();

    Page<Product> findByDeletedFalse(Pageable pageable);

    List<Product> findByRestaurantIdAndDeletedFalse(Integer restaurantId);

    Page<Product> findByRestaurantIdAndDeletedFalse(Integer restaurantId, Pageable pageable);

    List<Product> findByCategoryIdAndDeletedFalse(Integer categoryId);

    Page<Product> findByCategoryIdAndDeletedFalse(Integer categoryId, Pageable pageable);
}
