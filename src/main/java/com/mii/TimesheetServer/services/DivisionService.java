/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Division;
import com.mii.TimesheetServer.repositories.DivisionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author windak
 */
@Service
public class DivisionService {
   
    @Autowired
    DivisionRepository divisionRepository;

    public DivisionService(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }
    
    public List<Division> getAllDivision() {
        return divisionRepository.findAll();
    }
    
    public Optional<Division> findById(String id) {
        return divisionRepository.findById(id);
    }
    
    public boolean existById(String id){
        return divisionRepository.existsById(id);
    }
    
    public boolean saveDivision(Division division) {
        divisionRepository.save(division);
        return true;
    }
    
    public boolean deleteDivision(String id) {
        try {
            divisionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
