package com.foodygo.repository;

import com.foodygo.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Integer> {

    @Query("SELECT f.id.token FROM FcmToken f WHERE f.id.userId =:userId")
    List<String> findByUserId(@Param("userId") Integer userId);
}
