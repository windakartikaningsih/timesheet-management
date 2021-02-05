/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models.data;

import lombok.Data;

/**
 *
 * @author windak
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
