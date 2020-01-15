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

    public Button btn_write_done;
    public EditText et_write_schedule;
    public int week;
    private String project_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.schedule_add);

        set();

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        project_name = intent.getStringExtra("project_name");
        et_write_schedule.setText(data);
        week = intent.getIntExtra("schedule_week",0);
    }

    private void set() {
        et_write_schedule = (EditText) findViewById(R.id.write_schedule);
        btn_write_done = (Button) findViewById(R.id.write_done);
        btn_write_done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("result","Close Popup");
        postFirebaseDatabase(true);
        setResult(1,intent);
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

    private void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        if(add){
            Schedule_week.i++;
            Schedule_week.result.put("schedule"+ Schedule_week.i, et_write_schedule.getText().toString());
        }
        childUpdates.put("Schedule_Share"+"/project_list/" + project_name +"/"+ week + "주차", Schedule_week.result);
        mPostReference.updateChildren(childUpdates);
    }
}