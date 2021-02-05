/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.UserPermission;
import com.mii.TimesheetServer.repositories.UserPermissionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author windak
 */
@Service
public class UserPermissionService {
    
    @Autowired
    UserPermissionRepository upRepository;

    public UserPermissionService(UserPermissionRepository upRepository) {
        this.upRepository = upRepository;
    }
    
    public List<UserPermission> getAllUserPermission() {
        return upRepository.findAll();
    }
    
    public Optional<UserPermission> findById(Integer id){
        return upRepository.findById(id);
    }
    
    public boolean existById(Integer id){
        return upRepository.existsById(id);
    }
    
    public boolean saveUserPermission(UserPermission upermission) {
        upRepository.save(upermission);
        return true;
    }
    
    public boolean deleteUserPermission(Integer id) {
        try {
            upRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
