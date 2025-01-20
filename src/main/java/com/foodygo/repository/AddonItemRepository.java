package com.foodygo.repository;

import com.foodygo.entity.AddonItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddonItemRepository extends JpaRepository<AddonItem, Integer> {
}
