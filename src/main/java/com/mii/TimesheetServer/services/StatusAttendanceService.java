/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.StatusAttendance;
import com.mii.TimesheetServer.repositories.StatusAttendanceRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author BlackPearl
 */
@Service
public class StatusAttendanceService {
    
    @Autowired
    StatusAttendanceRepository attendanceRepository;
    
    public List<StatusAttendance> getAll(){
        return attendanceRepository.findAll();
    }
    
    public Optional<StatusAttendance> findById(Integer id){
        return attendanceRepository.findById(id);
    }
    
    public boolean existById(Integer id){
        return attendanceRepository.existsById(id);
    }
    
}
