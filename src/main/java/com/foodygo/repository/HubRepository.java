package com.foodygo.repository;

import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;

import com.foodygo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface HubRepository extends JpaRepository<Hub, Integer>, JpaSpecificationExecutor<Hub> {

    @Modifying
    @Query("update Hub set deleted = false where id = ?1")
    void unDeleted(int hubID);

    Hub findHubById(int id);

    Hub findHubByName(String name);

    Page<Hub> findAllByDeletedFalse(Pageable pageable);

    Page<Hub> findAll(Specification<Hub> spec, Pageable pageable);

}
