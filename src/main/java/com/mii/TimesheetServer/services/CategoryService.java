/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Category;
import com.mii.TimesheetServer.repositories.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author BlackPearl
 */
@Service
public class CategoryService {
    
    @Autowired
    CategoryRepository categoryRepository;
    
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }
    
    public Optional<Category> findById(Integer id){
        return categoryRepository.findById(id);
    }
    
    public boolean existById(Integer id){
        return categoryRepository.existsById(id);
    }
    
}
