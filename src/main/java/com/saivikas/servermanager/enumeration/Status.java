package com.saivikas.servermanager.enumeration;

import java.io.Serializable;

public enum Status implements Serializable {
    SERVER_UP("SERVER_UP"),
    SERVER_DOWN("SERVER_DOWN");
    private String status;
    Status(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
}
