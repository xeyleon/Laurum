package com.laurum.Faculty;

public class Faculty {
    private final Integer id;
    private final String first_name;
    private final String last_name;
    private final String email;

    public Faculty() {
        this.id = -1;
        this.first_name = "first_name";
        this.last_name = "last_name";
        this.email = "email";
    }
    public Faculty(Integer id, String first_name, String last_name, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public String getLastName() {
        return this.last_name;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getId() {
        return this.id;
    }

}