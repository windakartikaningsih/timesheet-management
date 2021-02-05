/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.StatusAttendance;
import com.mii.TimesheetServer.services.StatusAttendanceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author BlackPearl
 */
@RestController
@RequestMapping("api")
public class StatusAttendanceController {

    @Autowired
    private StatusAttendanceService attendanceService;

    @GetMapping("/status-attendance")
    public ResponseEntity getAll() {
        try {
            List<StatusAttendance> attendance = attendanceService.getAll();
            return new ResponseEntity<>(attendance, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }
    
    
    @GetMapping("/status-attendance/{id}")
    public ResponseEntity getById(@PathVariable Integer id){
        if(attendanceService.existById(id)){
            StatusAttendance attendance =  attendanceService.findById(id).get();
            return new ResponseEntity<>(attendance, HttpStatus.OK);
        }
        return new ResponseEntity("Data Not Found", HttpStatus.NOT_FOUND);
    }
    
}
