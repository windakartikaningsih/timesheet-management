/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.controllers;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Task;
import com.mii.TimesheetServer.entities.data.ResponseMessage;
import com.mii.TimesheetServer.services.EmployeeService;
import com.mii.TimesheetServer.services.TaskService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author BlackPearl
 */
@RestController
@RequestMapping("api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/task")
    public ResponseEntity getAll() {
        try {
            List<Task> task = taskService.getAll();
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/task/{id}")
    public ResponseEntity getById(@PathVariable String id) {
        if (taskService.existById(id)) {
            Task task = taskService.getById(id).get();
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/task/employee/{eId}")
    public ResponseEntity getTaskByEmployee(@PathVariable Integer eId) {
        try {
            List<Task> site = taskService.getTaskByEmployee(eId);
            return new ResponseEntity<>(site, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/task/{id}/{date}")
    public ResponseEntity getByEmployeeAndDate(@PathVariable("id") Integer id, @PathVariable("date") String stringDate) {
        if (employeeService.existById(id)) {
            try {
                Employees employee = employeeService.findById(id).get();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
                Task task = new Task();
                task.setDate(date);
                task.setEmployees(employee);
                List<Task> tasks = taskService.getByMonthAndYear(task);
                return new ResponseEntity<>(tasks, HttpStatus.OK);
            } catch (ParseException e) {
                return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/task/generate")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'RELATION_MANAGER')")
    public ResponseEntity<ByteArrayResource> generateReport(@RequestBody Task task) throws IOException {
        try {
            List<Task> tasks = taskService.getByMonthAndYear(task);
            byte[] excel = taskService.generateExcel(tasks);
            return new ResponseEntity(new ByteArrayResource(excel), HttpStatus.OK);
        } catch (ParseException e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/task")
    public ResponseEntity createTask(@RequestBody Task task) {
        if (!taskService.getByMonthAndEmployee(task.getDate(), task.getEmployees()).isPresent()) {
            try {
                task.setId("1");
                taskService.createTask(task);
                return new ResponseEntity<>(new ResponseMessage("Task Has Been Created", true), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>(new ResponseMessage("Failed to create", false), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("Data Already Exist", false), HttpStatus.CONFLICT);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity updateTask(@PathVariable String id, @RequestBody Task task) {
        if (taskService.existById(id)) {
            try {
                taskService.updateTask(id, task);
                return new ResponseEntity<>(new ResponseMessage<>("Task Has Been Updated", task), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity deleteTask(@PathVariable String id) {
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to Delete" + e, HttpStatus.BAD_REQUEST);
        }
    }

}
