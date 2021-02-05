/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Site;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.DivisionService;
import com.mii.TimesheetServer.services.EmployeeService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author windak
 */
@RestController
@RequestMapping("/api")
public class EmployeeController {
    
    @Autowired
    private EmployeeService service;
    
    @Autowired
    private DivisionService divisionService;
    
    @GetMapping("/employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employees>> getAllEmployee() {
        try {
            List<Employees> emp = service.getAllEmployee();
            return new ResponseEntity<>(emp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    } 
    
    @GetMapping("/employee/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RELATION_MANAGER')")
    public ResponseEntity getEmployeeById(@PathVariable Integer id) {
        if (service.existById(id)) {
            Employees employees = service.findById(id).get();
            return new ResponseEntity<>(employees, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/employee/site/{siteId}")
    @PreAuthorize("hasRole('RELATION_MANAGER')")
    public ResponseEntity<List<Employees>> getAllBySite(@PathVariable String siteId) {
        try {
            List<Employees> emp = service.findAllBySite(siteId);
            return new ResponseEntity<>(emp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/employee") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createEmployee(@RequestBody Employees employees) {
        try {
            System.out.println(employees);
            Optional<Employees> emp = service.createEmployee(employees);
            if (!emp.isPresent()){
                return new ResponseEntity<>(new ResponseMessage("Email Must Not Be Same", null), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(new ResponseMessage("Employee Has Been Created", emp), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(new ResponseMessage("Failed " + e, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/employee/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateEmployee(@PathVariable Integer id, @RequestBody  Employees employees) {
        if (service.existById(id)) {    
            try {
                service.updateEmployee(employees);
                return new ResponseEntity<>(new ResponseMessage("Employee Has Been updated", true), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(new ResponseMessage("Failed " + e, null), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("Failed ", null), HttpStatus.CONFLICT);
    }  
    
    @DeleteMapping("/employee/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteEmployee(@PathVariable Integer id) {
        try {
            service.deleteEmployee(id);
            return new ResponseEntity<>("Employee Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Employee Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}