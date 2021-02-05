/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.entities.data;

import lombok.Data;

/**
 *
 * @author Fahri
 * @param <T>
 */
@Data
public class ResponseData<T> {
    
    private T data;

    public ResponseData(T data) {
        this.data = data;
    }

    public ResponseData() {
    }
    
    
}
