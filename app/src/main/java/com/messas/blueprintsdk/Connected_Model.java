package com.messas.blueprintsdk;

public class Connected_Model {
    String name,mac,email;

    public Connected_Model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Connected_Model(String name, String mac, String email) {
        this.name = name;
        this.mac = mac;
        this.email = email;
    }
}
