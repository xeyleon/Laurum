package com.laurum.Courses;

public class Course {
    private final String id;
    private final String title;
    private final String desc;
    private final double credits;
    private int status;

    public Course() {
        this.id = "id";
        this.title = "title";
        this.desc = "desc";
        this.credits = 0.0;
        this.status = 0;
    }

    public Course(String id, String title, String desc, Double credits) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.credits = credits;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public Double getCredits() {
        return this.credits;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(int status) {this.status = status;}

}