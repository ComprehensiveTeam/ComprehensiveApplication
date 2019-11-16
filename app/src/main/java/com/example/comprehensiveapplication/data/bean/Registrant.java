package com.example.comprehensiveapplication.data.bean;

public class Registrant {
    String account;
    String vCode;
    String password;

    public Registrant() {
    }

    public Registrant(String password) {
        this.password = password;
    }

    public Registrant(String account, String vCode, String password) {
        this.account = account;
        this.vCode = vCode;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

