/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models;

/**
 *
 * @author windak
 */
public class Permission {
    
    private Integer id;
    private String permissionName;

    public Permission(Integer id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    public Permission() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
}
