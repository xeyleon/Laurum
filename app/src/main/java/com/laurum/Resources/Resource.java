package com.laurum.Resources;


public class Resource {
    private final Integer id;
    private final String title;
    private final String desc;
    private final String url;
    private final Integer icon;

    public Resource() {
        this.id = -1;
        this.title = "title";
        this.desc = "desc";
        this.url = "url";
        this.icon = 0;
    }
    public Resource(Integer id, String title, String desc, String url, Integer icon) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.icon = icon;
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

    public Integer getIcon() {
        return this.icon;
    }

}