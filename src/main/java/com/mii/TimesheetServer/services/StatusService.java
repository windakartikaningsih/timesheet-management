/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Status;
import com.mii.TimesheetServer.repositories.StatusRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author Fahri
 */
@Service
public class StatusService {
    
    private StatusRepository statusRepository;
    
    public List<Status> getAll(){
        return statusRepository.findAll();
    }
    
    public Optional<Status> getById(Integer id){
        return statusRepository.findById(id);
    }
}
