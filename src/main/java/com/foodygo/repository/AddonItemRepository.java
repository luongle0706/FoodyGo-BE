package com.foodygo.repository;

import com.foodygo.entity.AddonItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddonItemRepository extends JpaRepository<AddonItem, Integer> {
    List<AddonItem> findBySectionIdAndDeletedFalse(Integer sectionId);
    Page<AddonItem> findBySectionIdAndDeletedFalse(Integer sectionId, Pageable pageable);
    Optional<AddonItem> findByIdAndDeletedFalse(Integer id);
}
