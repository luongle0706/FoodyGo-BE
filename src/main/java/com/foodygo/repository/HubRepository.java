package com.foodygo.repository;

import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;

import com.foodygo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface HubRepository extends JpaRepository<Hub, Integer> {

    @Modifying
    @Query("update Hub set deleted = false where id = ?1")
    void unDeleted(int hubID);

    Hub findHubById(int id);

    List<Hub> findByBuildings(Building buildings);

    List<Hub> findByOrders(Order orders);

}
