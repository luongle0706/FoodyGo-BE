package com.foodygo.repository;

import com.foodygo.entity.Order;
import com.foodygo.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByCustomerId(Integer customerId, Pageable pageable);

    @Query("SELECT o FROM orders o WHERE o.employee.userID = :employeeId")
    Page<Order> findOrdersByEmployeeId(@Param("employeeId") Integer employeeId, Pageable pageable);

    Page<Order> findByRestaurantId(Integer restaurantId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}
