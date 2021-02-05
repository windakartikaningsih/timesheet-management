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
public class StatusAttendance {
    
    private Integer id;
    private String statusAttendanceName;

    public StatusAttendance() {
    }

    public StatusAttendance(Integer id, String statusAttendanceName) {
        this.id = id;
        this.statusAttendanceName = statusAttendanceName;
    }

    public StatusAttendance(Integer id) {
        this.id = id;
    }
    
}
