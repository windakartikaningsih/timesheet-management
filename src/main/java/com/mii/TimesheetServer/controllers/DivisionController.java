/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Division;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.DivisionService;
import com.mii.TimesheetServer.services.ManagerSiteService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author windak
 */
@RestController
@RequestMapping("/api")
public class DivisionController {
    
    @Autowired
    private DivisionService service;
    @Autowired
    private ManagerSiteService msService;
    
    @GetMapping("/division")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAllDivision() {
        try {
            List<Division> div = service.getAllDivision();
            return new ResponseEntity<>(div, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    } 
    
    @GetMapping("/division/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getDivisionById(@PathVariable String id) {
        if (service.existById(id)) {
            Division division = service.findById(id).get();
            return new ResponseEntity<>(division, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("/division") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createDivision(@RequestBody Division division) {
        try {
            division.setId("DV" + division.getId());
            service.saveDivision(division);
            return new ResponseEntity<>(new ResponseMessage("Division Has Been Created", division), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(new ResponseMessage("Failed " + e, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/division/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateDivision(@PathVariable String id, @RequestBody Division division) {
        if (service.existById(id)) {    
            try {
                service.saveDivision(division);
                return new ResponseEntity<>(new ResponseMessage("Division Has Been Updated", division), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(new ResponseMessage("Failed " + e, null), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Something Wrong ", HttpStatus.BAD_REQUEST);
    }  
    
    @DeleteMapping("/division/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteDivision(@PathVariable String id) {
        try {
            service.deleteDivision(id);
            return new ResponseEntity<>("Division Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Division Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}
