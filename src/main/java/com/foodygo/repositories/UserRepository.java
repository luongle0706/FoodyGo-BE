package com.foodygo.repositories;

import com.foodygo.pojos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    public User getUserByCodeVerify(String code);

    public User getUserByEmail(String email);

    public User getUserByUserID(int userID);

    public User getUserByPhone(String phone);

    @Modifying
    @Query("update user set enabled = true where userID = ?1")
    public void enabled(int userID);

    @Modifying
    @Query("update user set nonLocked = false where userID = ?1")
    public void locked(int userID);

    @Modifying
    @Query("update user set nonLocked = true where userID = ?1")
    public void unLocked(int userID);

    @Modifying
    @Query("update user set password = ?1 where email = ?2")
    public void setPasswordByEmail(String password, String email);

    @Modifying
    @Query("update user set password = ?1 where phone = ?2")
    public void setPasswordByPhone(String password, String phone);

    @Modifying
    @Query("update user set nonLocked = false where email = ?1")
    public void lockedByEmail(String email);
    
}
