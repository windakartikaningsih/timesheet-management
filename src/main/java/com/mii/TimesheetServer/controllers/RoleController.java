/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Role;
import com.mii.TimesheetServer.services.RoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author windak
 */
@RestController
@RequestMapping("/api")
public class RoleController {
    
    @Autowired
    private RoleService service;
    
    @GetMapping("/role")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity roleList() {
        try {
            List<Role> role = service.getAllRole();
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    } 
    
    @GetMapping("/role/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity roleById(@PathVariable Integer id) {
        if (service.existById(id)) {
            Role role = service.findById(id).get();
            return new ResponseEntity<>(role, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("/role")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createRole(@RequestBody Role role) {
        try {
            if (service.createRole(role) != true) {
                return new ResponseEntity<>("Role Failed To Created", HttpStatus.BAD_REQUEST);
            }
            service.createRole(role);
            return new ResponseEntity<>("Role Has Been Created", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Somthing Wrong", HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/role/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateRole(@RequestBody Role role) {
        try {
            if (service.createRole(role) != true) {
                return new ResponseEntity<>("Role Failed To Updated", HttpStatus.BAD_REQUEST);
            }
            service.createRole(role);
            return new ResponseEntity<>("Role Has Been Updated", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Somthing Wrong" + e, HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/role/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteRole(@PathVariable Integer id) {
        try {
            service.deleteRole(id);
            return new ResponseEntity<>("Role Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Role Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}
