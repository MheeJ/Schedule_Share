package com.example.schedule_share;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost {
    private String id,pw, name,birth, gender,project_name, project_info,project_member, project_notice, team;
    private Long project_date;

    public FirebasePost(){
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