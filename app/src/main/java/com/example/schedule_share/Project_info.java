package com.example.schedule_share;

import java.io.Serializable;

public class Project_info implements Serializable {

    private String project_name;
    private String project_info;
    private long project_date;

    public Project_info(){}

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_info() {
        return project_info;
    }

    public void setProject_info(String project_info) {
        this.project_info = project_info;
    }

    public long getProject_date() {
        return project_date;
    }

    public void setProject_date(long project_date) {
        this.project_date = project_date;
    }
}
