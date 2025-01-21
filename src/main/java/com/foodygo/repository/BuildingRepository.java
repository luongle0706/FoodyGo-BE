package com.foodygo.repository;

import com.foodygo.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BuildingRepository extends JpaRepository<Building, Integer> {
    Building findBuildingById(int id);

    Building findBuildingByName(String name);

    @Modifying
    @Query("update Building set deleted = false where id = ?1")
    void unDeleted(int buildingId);

}
