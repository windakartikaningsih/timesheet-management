/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.UserPermission;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.UserPermissionService;
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
public class UserPermissionController {

    @Autowired
    private UserPermissionService service;

    @GetMapping("/user-permission")
    public ResponseEntity userPermissionList() {
        try {
            List<UserPermission> emp = service.getAllUserPermission();
            return new ResponseEntity<>(emp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user-permission/{id}")
    public ResponseEntity userPermissionById(@PathVariable Integer id) {
        if (service.existById(id)) {
            UserPermission uper = service.findById(id).get();
            return new ResponseEntity<>(uper, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/user-permission")
    public ResponseEntity createUserPermission(@RequestBody UserPermission uper) {
        try {
            service.saveUserPermission(uper);
            return new ResponseEntity<>(new ResponseMessage("User Permission Has Been Created", uper), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(new ResponseMessage("failed: " + e, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user-permission/{id}")
    public ResponseEntity updateUserPermission(@PathVariable Integer id, @RequestBody UserPermission uper) {
        if (service.existById(id)) {
            try {
                service.saveUserPermission(uper);
                return new ResponseEntity<>(new ResponseMessage("User Permission Has Been Updated", uper), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(new ResponseMessage("Failed: " + e, null), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("failed", null), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/user-permission/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Integer id) {
        try {
            service.deleteUserPermission(id);
            return new ResponseEntity<>("User Permission Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User Permission Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}
