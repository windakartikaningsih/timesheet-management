/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.repositories;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.NoteHistory;
import com.mii.TimesheetServer.entities.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Fahri
 */
@Repository
public interface NoteHistoryRepository extends JpaRepository<NoteHistory, Integer> {

    List<NoteHistory> findAllByReport(Report report);

    @Query(value = "SELECT * FROM note_history n JOIN report r ON n.report_id = r.id "
            + " JOIN employees e ON r.employee_id = e.id "
            + " WHERE e.id = ?2 AND n.is_read = ?1", nativeQuery = true)
    List<NoteHistory> findAllByIsReadAndEmployees(Boolean isRead, Integer id);
    @Query(value = "SELECT * FROM note_history n JOIN report r ON n.report_id = r.id "
            + " JOIN employees e ON r.employee_id = e.id "
            + " WHERE e.id = ?1", nativeQuery = true)
    List<NoteHistory> findAllByEmployee(Integer id);
}
