package com.foodygo.repository;

import com.foodygo.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {

    List<FcmToken> findByUserUserID(Integer id);
}
