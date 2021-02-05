/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.repositories;

import com.mii.TimesheetServer.entities.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Fahri
 */
@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Integer>{
    
}
