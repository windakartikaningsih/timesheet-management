/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Site;
import com.mii.TimesheetServer.repositories.SiteRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author windak
 */
@Service
public class SiteService {

    @Autowired
    SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    public List<Site> getAllSite() {
        return siteRepository.findAll();
    }

    public Optional<Site> findById(String id) {
        return siteRepository.findById(id);
    }
    
    public List<Site> getSiteByRM(Integer rmId) {
        return siteRepository.getSiteByRM(rmId);
    }
    
    public boolean existById(String id) {
        return siteRepository.existsById(id);
    }
    
    public boolean saveSite(Site site) {
        siteRepository.save(site);
        return true;
    }
    
    public boolean deleteSite(String id) {
        try {
            siteRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
