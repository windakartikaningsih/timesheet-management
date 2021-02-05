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
public class ManagerSite {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Site site;

    public ManagerSite(Integer id, String firstName, String lastName, String email, Site site) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.site = site;
    }

    public ManagerSite() {
    }
    
    @Override
    public boolean equals(Object obj) {
        return !super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
