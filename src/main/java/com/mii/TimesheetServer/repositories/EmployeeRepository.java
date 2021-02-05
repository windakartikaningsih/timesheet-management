/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.repositories;

import com.mii.TimesheetServer.entities.Employees;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author windak
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employees, Integer>{
    
    boolean existsByEmail(String email);
    
    Optional<Employees> findByEmail(String email);
    
    @Query(value = "SELECT * FROM employees e JOIN division d ON e.division_id = d.id LEFT JOIN manager_site ms ON ms.id = d.manager_id LEFT JOIN site s ON s.id = ms.site_id WHERE s.id = ?1", nativeQuery = true)
    List<Employees> findAllBySite(String siteId);
    
}
