package com.foodygo.repository;

import com.foodygo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByIdAndDeletedFalse(Integer id);

    Optional<Category> findByNameIgnoreCaseAndDeletedFalse(String name);

    List<Category> findByNameContainingIgnoreCase(String name);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Category> findByRestaurantIdAndDeletedFalse(Integer restaurantId);
    Page<Category> findByRestaurantIdAndDeletedFalse(Integer restaurantId, Pageable pageable);

    List<Category> findByDeletedFalse();
    Page<Category> findByDeletedFalse(Pageable pageable);
}
