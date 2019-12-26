package com.muttychat.muttychat;

public class UserHolder {

    private String email;
    private String location;
    private String name;

    private UserHolder(){

    }

    public UserHolder(String email, String location, String name) {
        this.email = email;
        this.location = location;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
