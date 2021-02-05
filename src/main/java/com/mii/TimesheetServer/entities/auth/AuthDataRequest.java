/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities.auth;

import lombok.Data;

/**
 *
 * @author Fahri
 */
@Data
public class AuthDataRequest {

    private String username;
    private String password;

    public AuthDataRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDataRequest() {
    }

}
