package com.foodygo.repository;

import com.foodygo.entity.Customer;
import com.foodygo.entity.Hub;
import com.foodygo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerById(Integer id);

    Page<Customer> findAllByBuildingId(Integer building_id, Pageable pageable);

    Page<Customer> findAllByDeletedFalse(Pageable pageable);

    List<Customer> findByDeletedIsFalse();

    Optional<Customer> findByUserUserID(Integer userID);
}
