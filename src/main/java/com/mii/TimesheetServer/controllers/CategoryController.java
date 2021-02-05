/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Category;
import com.mii.TimesheetServer.services.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author BlackPearl
 */
@RestController
@RequestMapping ("api")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/category")
    public ResponseEntity getAll() {
        try {
            List<Category> attendance = categoryService.getAll();
            return new ResponseEntity<>(attendance, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/category/{id}")
    public ResponseEntity getById(@PathVariable Integer id){
        if(categoryService.existById(id)){
            Category category =  categoryService.findById(id).get();
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
        return new ResponseEntity("Data Not Found", HttpStatus.NOT_FOUND);
    }
    
}
