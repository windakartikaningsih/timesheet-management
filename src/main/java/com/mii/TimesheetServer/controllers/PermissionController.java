/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Permission;
import com.mii.TimesheetServer.services.PermissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class PermissionController {

    @Autowired
    private PermissionService service;

    @GetMapping("/permission")
    public ResponseEntity permissionList() {
        try {
            List<Permission> permission = service.getAllPermission();
            return new ResponseEntity<>(permission, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity permissionById(@PathVariable Integer id) {
        if (service.existById(id)) {
            Permission permission = service.findById(id).get();
            return new ResponseEntity<>(permission, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/permission")
    public ResponseEntity createPermission(@RequestBody Permission permission) {
        try {
            if (service.createPermission(permission) != true) {
                return new ResponseEntity<>("Permission Failed To Created", HttpStatus.BAD_REQUEST);
            }
            service.createPermission(permission);
            return new ResponseEntity<>("Permission Has Been Created", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something Wrong", HttpStatus.BAD_REQUEST);
        }
    }

    //NEW UPDATE
    @PostMapping("/permission/{id}")
    public ResponseEntity updatePermission(@PathVariable Integer id, @RequestBody Permission permission) {
        if (service.existById(id)) {
            try {
                service.updatePermissions(permission);
                return new ResponseEntity<>("Permission Has Been Update", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to Update", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/permission/{id}")
    public ResponseEntity deletePermission(@PathVariable Integer id) {
        try {
            service.deletePermission(id);
            return new ResponseEntity<>("Permission Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Permission Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}
