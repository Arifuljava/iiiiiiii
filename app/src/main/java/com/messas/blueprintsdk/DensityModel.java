package com.messas.blueprintsdk;

public class DensityModel {
    String speed,density,email;

    public DensityModel() {
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DensityModel(String speed, String density, String email) {
        this.speed = speed;
        this.density = density;
        this.email = email;
    }
}
