/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Role;
import com.mii.TimesheetServer.repositories.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author windak
 */
@Service
public class RoleService {
    
    @Autowired
    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
    
    //NEW
    public Optional<Role> findById(Integer id){
        return roleRepository.findById(id);
    }
    
    public boolean existById(Integer id){
        return roleRepository.existsById(id);
    }
    
    public boolean createRole(Role role) {
        Optional<Role> r = roleRepository.findById(role.getId());
        try {
            if (!r.isPresent()) {
                roleRepository.save(role);
            } else {
                roleRepository.save(role);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }
    
    public boolean updateRole(Role role) {
        Optional<Role> r = roleRepository.findById(role.getId());
        try {
            if (!r.isPresent()) {
                roleRepository.save(role);
            } else {
                roleRepository.save(role);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }
    
    public boolean deleteRole(Integer id) {
        try {
            roleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
