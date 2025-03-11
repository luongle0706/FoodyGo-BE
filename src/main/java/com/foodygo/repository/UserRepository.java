package com.foodygo.repository;

import com.foodygo.entity.Hub;
import com.foodygo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.hub.id = :hubId")
    List<User> findUsersByHubId(@Param("hubId") Integer hubId);

    Optional<User> findByEmailAndDeletedIsFalse(String email);

    User getUserByEmail(String email);

    User getUserByUserID(int userID);

    User getUserByPhone(String phone);

    Page<User> findAllByDeletedFalse(Pageable pageable);

    List<User> findByDeletedIsFalse();

    @Modifying
    @Query("update User set enabled = true where userID = ?1")
    void enabled(int userID);

    @Modifying
    @Query("update User set nonLocked = false where userID = ?1")
    void locked(int userID);

    @Modifying
    @Query("update User set nonLocked = true where userID = ?1")
    void unLocked(int userID);

    @Modifying
    @Query("update User set password = ?1 where email = ?2")
    void setPasswordByEmail(String password, String email);

    @Modifying
    @Query("update User set password = ?1 where phone = ?2")
    void setPasswordByPhone(String password, String phone);

    @Modifying
    @Query("update User set nonLocked = false where email = ?1")
    void lockedByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    int countNumberOfRegisterToday();

    boolean existsByEmail(String email);
}
