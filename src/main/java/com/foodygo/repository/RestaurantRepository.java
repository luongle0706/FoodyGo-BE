package com.foodygo.repository;

import com.foodygo.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>, JpaSpecificationExecutor<Restaurant> {
    Optional<Restaurant> findByIdAndDeletedFalse(Integer id);

    List<Restaurant> findByDeletedFalse();

    Page<Restaurant> findByDeletedFalse(Pageable pageable);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Restaurant> findByOwnerUserID(Integer userID);
}
