package com.laurum;

public class Faculty {
    private final Integer id;
    private final String name;
    private final String title;
    private final String department;
    private final String email;

    public Faculty() {
        this.id = -1;
        this.name = "name";
        this.title = "title";
        this.department = "department";
        this.email = "email";
    }
    public Faculty(Integer id, String name, String title, String department, String email) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.department = department;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDepartment() {
        return this.department;
    }
    public String getEmail() {
        return this.email;
    }
    public Integer getId() {
        return this.id;
    }

}