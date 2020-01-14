package com.example.schedule_share;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DowonYoon on 2017-07-11.
 */
@IgnoreExtraProperties
class FirebasePost {
    public String id;
    public String pw;
    public String name;
    public String birth;
    public String gender;
    public String project_name;
    public String project_info;
    public Long project_date;
    public String project_member;
    public String project_notice;
    public String schedule;
    public String team;


    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String id, String pw, String name, String birth, String gender, String team) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.team = team;
    }

    public FirebasePost(String project_name, String project_info, Long project_date, String project_member, String project_notice) {
        this.project_name = project_name;
        this.project_info = project_info;
        this.project_date = project_date;
        this.project_member = project_member;
        this.project_notice = project_notice;
    }

    public FirebasePost(String notice) {
        this.schedule = notice;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("name", name);
        result.put("birth", birth);
        result.put("gender", gender);
        result.put("team",team);
        return result;
    }

    @Exclude
    public Map<String, Object> toScheduleMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("project_name", project_name);
        result.put("project_info", project_info);
        result.put("project_date", project_date);
        result.put("project_member",project_member);
        result.put("project_notice",project_notice);
        return result;
    }
}