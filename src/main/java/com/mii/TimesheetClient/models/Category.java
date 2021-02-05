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
public class Category {
    
    private Integer id;
    private String categoryName;

    public Category(Integer id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public Category(Integer id) {
        this.id = id;
    }
    
    public Category() {
    }
    
}
