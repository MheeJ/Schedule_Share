package com.example.schedule_share;

import java.io.Serializable;

public class Project_item implements Serializable {

    private String project_name;
    private String project_info;
    private long project_date;

    public Project_item(){}

    public String getProject_name() {
        return project_name;
    }

    public String getProject_info() {
        return project_info;
    }

    public long getProject_date() {
        return project_date;
    }
}
