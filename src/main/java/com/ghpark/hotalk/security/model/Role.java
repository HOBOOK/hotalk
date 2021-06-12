package com.ghpark.hotalk.security.model;

/**
 * 권한 타입
 * @author pkh879
 */
public enum Role {
    USER, ADMIN;

    public String authority(){
        return "ROLE_" + this.name();
    }
}
