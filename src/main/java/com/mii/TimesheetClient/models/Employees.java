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
public class Employees {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Division division;

    public Employees(Integer id, String firstName, String lastName, String email, String phoneNumber, Division division) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.division = division;
    }

    public Employees(Integer id) {
        this.id = id;
    }
    
    public Employees() {
    }
    
}
