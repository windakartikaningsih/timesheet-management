/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.ManagerSite;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.ManagerSiteService;
import com.mii.TimesheetServer.services.SiteService;
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
public class ManagerSiteController {
    
    @Autowired
    private ManagerSiteService service;
    
    @Autowired
    private SiteService siteService;
    
    @GetMapping("/manager-site")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAllManagerSite() {
        try {
            List<ManagerSite> emp = service.getAllManagerSite();
            return new ResponseEntity<>(emp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    } 
    
    @GetMapping("/manager-site/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getManagerSiteById(@PathVariable Integer id) {
        if (service.existById(id)) {
            ManagerSite manager = service.findById(id).get();
            return new ResponseEntity<>(manager, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("/manager-site") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createManagerSite(@RequestBody ManagerSite manager) {
        try {
            service.saveManagerSite(manager);
            return new ResponseEntity<>(new ResponseMessage("Manager Site Has Been Created", manager), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(new ResponseMessage("failed: "+e, null), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/manager-site/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateManagerSite(@PathVariable Integer id, @RequestBody ManagerSite manager) {
        if (service.existById(id)) {    
            try {
                manager.setId(id);
                service.saveManagerSite(manager);
                return new ResponseEntity<>(new ResponseMessage("Manager Site Has Been Updated", manager), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(new ResponseMessage("failed: "+e, null), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("failed", null), HttpStatus.BAD_REQUEST);
    } 
    
    @DeleteMapping("/manager-site/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteManagerSite(@PathVariable Integer id) {
        try {
            service.deleteManagerSite(id);
            return new ResponseEntity<>("Manager Site Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Manager Site Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}
