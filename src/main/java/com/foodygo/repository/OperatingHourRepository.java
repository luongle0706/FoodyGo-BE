package com.foodygo.repository;

import com.foodygo.entity.OperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatingHourRepository extends JpaRepository<OperatingHour, Integer> {
    List<OperatingHour> findByRestaurantId(Integer restaurantId);
}
