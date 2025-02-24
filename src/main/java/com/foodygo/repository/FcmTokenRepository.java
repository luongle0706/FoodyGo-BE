package com.foodygo.repository;

import com.foodygo.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Integer> {

    @Query("SELECT f.id.token FROM FcmToken f WHERE f.id.userId =:userId")
    List<String> findByUserId(@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query("UPDATE FcmToken f SET f.loggedIn = false WHERE f.id.userId = :userId")
    void logoutUser(@Param("userId") Integer userId);
}
