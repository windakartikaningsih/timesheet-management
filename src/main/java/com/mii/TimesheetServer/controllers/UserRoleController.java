/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.UserRole;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.UserRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author windak
 */
@RestController
@RequestMapping("/api")
public class UserRoleController {

    @Autowired
    private UserRoleService service;

    @GetMapping("/user-role")
    public ResponseEntity userRoleList() {
        try {
            List<UserRole> emp = service.getAllUserRole();
            return new ResponseEntity<>(emp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user-role/{id}")
    public ResponseEntity userRoleById(@PathVariable Integer id) {
        if (service.existById(id)) {
            UserRole urole = service.findById(id).get();
            return new ResponseEntity<>(urole, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/user-role")
    public ResponseEntity createUserRole(@RequestBody UserRole urole) {
        try {
            service.saveUserRole(urole);
            return new ResponseEntity<>(new ResponseMessage("User Role Has Been Created", urole), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(new ResponseMessage("failed: " + e, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user-role/{id}")
    public ResponseEntity updateUserRole(@PathVariable Integer id, @RequestBody UserRole urole) {
        if (service.existById(id)) {
            try {
                urole.setId(id);
                service.saveUserRole(urole);
                return new ResponseEntity<>(new ResponseMessage("User Role Has Been Updated", urole), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(new ResponseMessage("Failed: " + e, null), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("failed", null), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/user-role/{id}")
    public ResponseEntity deleteUserRole(@PathVariable Integer id) {
        try {
            service.deleteUserRole(id);
            return new ResponseEntity<>("User Role Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User Role Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}
