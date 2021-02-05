/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Permission;
import com.mii.TimesheetServer.repositories.PermissionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author windak
 */
@Service
public class PermissionService {
    
    @Autowired
    PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    
    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }
    
    public Permission findById2(Integer id) {
        return permissionRepository.getOne(id);
    }
    
    //NEW
    public Optional<Permission> findById(Integer id){
        return permissionRepository.findById(id);
    }
    
    public boolean existById(Integer id){
        return permissionRepository.existsById(id);
    }
    
    public boolean createPermission(Permission permission) {
        Optional<Permission> izin = permissionRepository.findById(permission.getId());
        try {
            if (!izin.isPresent()) {
                permissionRepository.save(permission);
            } else {
                permissionRepository.save(permission);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }
    
    public boolean updatePermissions(Permission permission) {
        Optional<Permission> izin =  permissionRepository.findById(permission.getId());
        try {
            if (!izin.isPresent()) {
                permissionRepository.save(permission);
            } else {
                permissionRepository.save(permission);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }
    
    public boolean deletePermission(Integer id) {
        try {
            permissionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
