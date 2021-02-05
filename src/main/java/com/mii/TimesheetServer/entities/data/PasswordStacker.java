/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities.data;

import lombok.Data;

/**
 *
 * @author windak
 */
@Data
public class PasswordStacker {

    private String oldPassword;
    private String newPassword;

    public PasswordStacker(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public PasswordStacker() {
    }

}
