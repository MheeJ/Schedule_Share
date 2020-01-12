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

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String id, String pw, String name, String birth, String gender) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("name", name);
        result.put("birth", birth);
        result.put("gender", gender);
        return result;
    }
}