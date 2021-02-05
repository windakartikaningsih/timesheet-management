/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models;

import lombok.Data;

/**
 *
 * @author BlackPearl
 */
@Data
public class Task {

    private String id;
    private String projectName;
    private String date;
    private String startHour;
    private String endHour;
    private String activity;
    private Category category;
    private StatusAttendance statusAttendance;
    private Employees employees;

    public Task(String id, String projectName, String date, String startHour, String endHour, String activity, Category category, StatusAttendance statusAttendance, Employees employees) {
        this.id = id;
        this.projectName = projectName;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.activity = activity;
        this.category = category;
        this.statusAttendance = statusAttendance;
        this.employees = employees;
    }

    public Task() {
    }
}
