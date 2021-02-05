/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.repositories;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Report;
import java.util.Date;
import java.util.List;
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
public interface ReportRepository extends JpaRepository<Report, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO report(name, report_date, document_dir, upload_date, report_status_id, employee_id) VALUES(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insertReport(String name, Date reportDate, String dir, Date uploadDate, Integer reportStatusId, Integer employeeId);

    Optional<Report> findByReportDate(Date reportDate);

    List<Report> findAllByReportDateBetweenAndEmployees(Date dateStart, Date dateEnd, Employees employees);
    
    List<Report> findAllByEmployees(Employees employees);
}
