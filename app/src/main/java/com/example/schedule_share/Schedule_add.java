package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Schedule_add extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mPostReference;

    public Button write_done;
    public EditText write_schedule;
    public int week;
    private String project_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.schedule_add);

        write_schedule = (EditText) findViewById(R.id.write_schedule);
        write_done = (Button) findViewById(R.id.write_done);
        write_done.setOnClickListener(this);

        Intent intent8 = getIntent();
        String data = intent8.getStringExtra("data");
        project_name = intent8.getStringExtra("project_name");
        write_schedule.setText(data);
        week = intent8.getIntExtra("schedule_week",0);
    }

    @Override
    public void onClick(View v) {
        Intent intent7 = new Intent();
        intent7.putExtra("result","Close Popup");
        setResult(RESULT_OK,intent7);
        postFirebaseDatabase(true);

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        return;
    }

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            Project_week.i++;
            Project_week.result.put("schedule"+Project_week.i, write_schedule.getText().toString());
        }
        childUpdates.put("Schedule_Share"+"/project_list/" + project_name +"/"+ week + "주차", Project_week.result);
        mPostReference.updateChildren(childUpdates);
    }
}
