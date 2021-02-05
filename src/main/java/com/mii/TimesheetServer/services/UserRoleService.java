/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.UserRole;
import com.mii.TimesheetServer.repositories.UserRoleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author windak
 */
@Service
public class UserRoleService {
    
    @Autowired
    UserRoleRepository urRepository;

    public UserRoleService(UserRoleRepository urRepository) {
        this.urRepository = urRepository;
    }
    
    public List<UserRole> getAllUserRole() {
        return urRepository.findAll();
    }
    
    public Optional<UserRole> findById(Integer id){
        return urRepository.findById(id);
    }
    
    public boolean existById(Integer id){
        return urRepository.existsById(id);
    }
    
    public boolean saveUserRole(UserRole urole) {
        urRepository.save(urole);
        return true;
    }
    
    public boolean deleteUserRole(Integer id) {
        try {
            urRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
