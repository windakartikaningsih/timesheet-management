/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.repositories;

import com.mii.TimesheetServer.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Fahri
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user(id, username, password, is_verified, status_id) VALUES(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void insertUser(Integer id, String username, String password, boolean isVerified, Integer statusId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET username = ?1, password = ?2, verification_code = ?3, is_verified = ?4, status_id = ?5 WHERE id = ?6", nativeQuery = true)
    void updateUser(String username, String password, String vCode, boolean isVerified, Integer statusId, Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user WHERE user.id = ?1", nativeQuery = true)
    void deleteUser(Integer id);
    
    @Query(value = "SELECT * FROM user u JOIN employees e ON u.id = e.id WHERE e.email = ?1", nativeQuery = true)
    User getByEmail(String email);
    
    Optional<User> findByUsername(String username);
   
    boolean existsByUsername(String username);
}
