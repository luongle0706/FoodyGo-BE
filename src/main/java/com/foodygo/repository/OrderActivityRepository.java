package com.foodygo.repository;

import com.foodygo.entity.OrderActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderActivityRepository extends JpaRepository<OrderActivity, Integer> {
    List<OrderActivity> findByOrderIdOrderByTimeDesc(Integer orderId);

}
