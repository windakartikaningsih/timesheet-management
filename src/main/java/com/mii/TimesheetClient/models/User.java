/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models;

import lombok.Data;

/**
 *
 * @author windak
 */
@Data
public class User {
    
    private Integer id;
    private String username;
    private Status status;
    private Employees employees;

    public User(Integer id, String username, Status status, Employees employees) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.employees = employees;
    }

    

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
