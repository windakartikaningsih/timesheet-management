/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.ReportStatus;
import com.mii.TimesheetServer.services.ReportStatusService;
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
public class ReportStatusController {

    @Autowired
    private ReportStatusService statusService;

    @GetMapping("/report-status")
    public ResponseEntity getAll() {
        try {
            List<ReportStatus> reportStatus = statusService.getAll();
            return new ResponseEntity<>(reportStatus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/report-status/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
        if (statusService.existById(id)) {
            ReportStatus reportStatus = statusService.getById(id).get();
            return new ResponseEntity<>(reportStatus, HttpStatus.OK);
        }
        return new ResponseEntity("Data Not Found", HttpStatus.NOT_FOUND);
    }

}
