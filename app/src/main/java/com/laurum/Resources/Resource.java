package com.laurum.Resources;


public class Resource {
    private final Integer id;
    private final String title;
    private final String desc;
    private final String url;

    public Resource() {
        this.id = -1;
        this.title = "title";
        this.desc = "desc";
        this.url = "url";
    }
    public Resource(Integer id, String title, String desc, String url) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
    }

    public Integer getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getUrl() {
        return this.url;
    }

}