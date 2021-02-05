/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.repositories;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Task;
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
 * @author windak
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, String>{
    
    List<Task> findAllByDateBetweenAndEmployees(Date dateStart, Date dateEnd, Employees employees);
    Optional<Task> findByDateAndEmployees(Date date, Employees employees);
    
    @Query(value = "SELECT * FROM task t JOIN employees e ON t.employee_id = e.id WHERE e.id = ?1", nativeQuery = true)
    List<Task> getTaskByEmployee(Integer eId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE task t SET t.project_name = ?1, t.date = ?2, t.start_hour = ?3, t.end_hour = ?4, t.activity = ?5, t.status_attendance_id = ?6, t.category_id = ?7 WHERE t.id = ?8", nativeQuery = true)
    void updateTask(String project_name, Date date, Date startHour, Date endHour, String activity, Integer statusAttendanceId, Integer categoryId, String id);
}
