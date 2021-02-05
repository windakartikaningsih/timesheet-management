/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.ManagerSite;
import com.mii.TimesheetServer.repositories.ManagerSiteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author windak
 */
@Service
public class ManagerSiteService {
    
    @Autowired
    ManagerSiteRepository msRepository;

    public ManagerSiteService(ManagerSiteRepository msRepository) {
        this.msRepository = msRepository;
    }
    
    public List<ManagerSite> getAllManagerSite() {
        return msRepository.findAll();
    }
    
    public Optional<ManagerSite> findById(Integer id) {
        return msRepository.findById(id);
    }
    
    public boolean existById(Integer id){
        return msRepository.existsById(id);
    }
    
    public boolean saveManagerSite(ManagerSite ms) {
        msRepository.save(ms);
        return true;
    }
    
    public boolean deleteManagerSite(Integer id) {
        try {
            msRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
