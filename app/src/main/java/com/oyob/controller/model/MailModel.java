package com.oyob.controller.model;

/**
 * Created by Trush on 30/03/16.
 */
public class MailModel {
    String email;
    String password;
    boolean signUp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSignUp() {
        return signUp;
    }

    public void setSignUp(boolean signUp) {
        this.signUp = signUp;
    }
}
