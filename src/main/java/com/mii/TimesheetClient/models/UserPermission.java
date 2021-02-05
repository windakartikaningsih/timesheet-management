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
public class UserPermission {
    private Integer id;
    private User user;
    private Permission permission;

    public UserPermission(Integer id, User user, Permission permission) {
        this.id = id;
        this.user = user;
        this.permission = permission;
    }

    public UserPermission() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
    
}
