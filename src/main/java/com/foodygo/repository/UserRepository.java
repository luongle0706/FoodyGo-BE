package com.foodygo.repository;

import com.foodygo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    User getUserByEmail(String email);

    User getUserByUserID(int userID);

    User getUserByPhone(String phone);

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

}
