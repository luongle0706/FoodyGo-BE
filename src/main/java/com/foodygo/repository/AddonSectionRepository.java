package com.foodygo.repository;

import com.foodygo.entity.AddonSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddonSectionRepository extends JpaRepository<AddonSection, Integer>, JpaSpecificationExecutor<AddonSection> {
    Optional<AddonSection> findByIdAndDeletedFalse(Integer id);
}
