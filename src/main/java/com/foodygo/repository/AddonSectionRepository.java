package com.foodygo.repository;

import com.foodygo.entity.AddonSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddonSectionRepository extends JpaRepository<AddonSection, Integer> {
}
