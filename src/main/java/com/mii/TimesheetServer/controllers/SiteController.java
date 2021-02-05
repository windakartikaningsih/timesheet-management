/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Site;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.EmployeeService;
import com.mii.TimesheetServer.services.SiteService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author windak
 */
@RestController
@RequestMapping("/api")
public class SiteController {

    @Autowired
    SiteService service;
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/site")
    public ResponseEntity getAllSite() {
        try {
            List<Site> sites = service.getAllSite();
            return new ResponseEntity<>(sites, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/site/{id}")
    public ResponseEntity getSiteById(@PathVariable String id) {
        if (service.existById(id)) {
            Site site = service.findById(id).get();
            return new ResponseEntity<>(site, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/site/rm/{rmId}")
    @PreAuthorize("hasRole('RELATION_MANAGER')")
    public ResponseEntity getSiteByRM(@PathVariable Integer rmId) {
        try {
            List<Site> site = service.getSiteByRM(rmId);
            return new ResponseEntity<>(site, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.BAD_REQUEST);
        }        
    }

    @PostMapping("/site") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createSite(@RequestBody Site site) {
        try {
            site.setId("ST" + site.getId());
            service.saveSite(site);
            return new ResponseEntity<>(new ResponseMessage("Site Has Been Created", site), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(new ResponseMessage("Site Failed To Created " + e, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/site/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateSite(@PathVariable String id, @RequestBody Site site) {
        if (service.existById(id)) {    
            try {
                service.saveSite(site);
                return new ResponseEntity<>(new ResponseMessage("Site Has Been Updated", site), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(new ResponseMessage("Site Failed To Updated " + e, null), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Something Wrong ", HttpStatus.BAD_REQUEST);
    }  
    
    @DeleteMapping("/site/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteSite(@PathVariable String id) {
        try {
            service.deleteSite(id);
            return new ResponseEntity<>("Site Has Been Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Site Failed To Delete", HttpStatus.BAD_REQUEST);
        }
    }
}
