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
public class Division {
    
    private String id;
    private String name;
    private ManagerSite managerSite;

    public Division(String id, String name, ManagerSite managerSite) {
        this.id = id;
        this.name = name;
        this.managerSite = managerSite;
    }

    public Division() {
    }
}
