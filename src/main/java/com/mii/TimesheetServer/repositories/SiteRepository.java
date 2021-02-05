/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.repositories;

import com.mii.TimesheetServer.entities.Site;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author windak
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, String> {
    
    @Query(value = "SELECT * FROM site s JOIN employees e ON s.relation_manager_id = e.id WHERE s.relation_manager_id = ?1", nativeQuery = true)
    List<Site> getSiteByRM(Integer rmId);
}
