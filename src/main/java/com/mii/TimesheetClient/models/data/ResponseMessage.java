/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetClient.models.data;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 *
 * @author windak
 * @param <T>
 */
@Data
public class ResponseMessage<T> extends ResponseData<T> {

    private String message;

    public ResponseMessage(String message, T data) {
        super(data);
        this.message = message;
    }

    public ResponseMessage() {
    }

}
