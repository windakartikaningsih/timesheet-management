/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Division;
import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Site;
import com.mii.TimesheetServer.repositories.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author windak
 */
@Service
public class EmployeeService {
    
    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public List<Employees> getAllEmployee() {
        return employeeRepository.findAll();
    }
    
    public Optional<Employees> findById(Integer id) {
        return employeeRepository.findById(id);
    }
    
    public List<Employees> findAllBySite(String siteId) {
        return employeeRepository.findAllBySite(siteId);
    }
    
    public boolean existById(Integer id){
        return employeeRepository.existsById(id);
    }
    
    public Optional<Employees> createEmployee(Employees employees) {
        if (!employeeRepository.existsByEmail(employees.getEmail())) {
            employeeRepository.save(employees);
            return employeeRepository.findByEmail(employees.getEmail());
        }
        return null;
    }
    
    public boolean updateEmployee(Employees employees) {
        employeeRepository.save(employees);
        return true;
    }
    
    public Employees deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
        return employeeRepository.getOne(id);
    }
}
