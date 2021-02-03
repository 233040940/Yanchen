package com.local.security.entity.dto;

import java.io.Serializable;

/**
 * @Create-By: yanchen 2021/1/13 10:38
 * @Description: TODO
 */
public class LoginDTO implements Serializable {
    String password;
    String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
