package com.foodygo.repository;

import com.foodygo.entity.OrderActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderActivityRepository extends JpaRepository<OrderActivity, Integer> {
    Page<OrderActivity> findByOrderIdOrderByTimeDesc(Integer orderId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM OrderActivity oa WHERE oa.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);
}
