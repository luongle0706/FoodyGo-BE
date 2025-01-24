package com.foodygo.repository;

import com.foodygo.entity.Building;
import com.foodygo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BuildingRepository extends JpaRepository<Building, Integer> {
    Building findBuildingById(int id);

    Building findBuildingByName(String name);

    @Modifying
    @Query("update Building set deleted = false where id = ?1")
    void unDeleted(int buildingId);

    Page<Building> findAllByDeletedFalse(Pageable pageable);

    Page<Building> findAllByHub_Id(Integer hubId, Pageable pageable);

}
