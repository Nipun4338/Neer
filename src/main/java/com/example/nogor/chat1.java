package com.example.nogor;

public class chat1 {
    String name, id, dp;

    public chat1()
    {

    }

    public chat1(String name, String id, String dp) {
        this.name = name;
        this.id = id;
        this.dp = dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
