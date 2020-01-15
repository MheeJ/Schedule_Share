package com.example.schedule_share;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Project_add extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mPostReference;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private TextView tv_name, tv_period, tv_goal, tv_member,tv_start,tv_finish, tv_start_date,tv_finish_date,tv_input_period,tv_memeber_num,tv_period_1,tv_Member_list;
    private EditText et_input_name, et_input_goal;
    private Button btn_done,btn_start_button,btn_finish_button,btn_Add_member;
    private DatePickerDialog.OnDateSetListener callbackMethod, callbackMethod2;
    private final static int CODE=1;
    private String str_project_name,str_date1,str_date2,str_Days, str_project_info;
    private String str_project_member="멤버";
    private String str_project_notice = "공지사항";
    private long project_date,calDateDays;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_project);
        findviewlist();
        onclicklist();
        this.InitializeListener();
        this.InitializeListener2();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    }

    public void InitializeListener(){
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                tv_start_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        };
    }

    public void OnClickHandler(View view){
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2020, 1,6);
        dialog.show();
    }

    public void InitializeListener2(){
        callbackMethod2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                tv_finish_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);

                calDate();

                long i = calDateDays/7;
                project_date = i;
                str_Days = Long.toString(i) ;
                tv_input_period.setText(str_Days);
            }
        };
    }

    public void OnClickHandler2(View view){
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod2, 2020, 1,6);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done :
                String real_name = et_input_name.getText().toString();
                Project_item projectInfo = new Project_item();
                Intent intent2 = new Intent(this,Project_list.class);
                intent2.putExtra("name", projectInfo);
                intent2.putExtra("name_member",str_project_member);
                startActivity(intent2);
                postFirebaseDatabase(true);
                update_team();
                break;

            case R.id.add_member:
                Intent intent3 = new Intent(this,Add_User.class);
                intent3.putExtra("addr",tv_Member_list.getText().toString());
                intent3.putExtra("notice",str_project_notice);
                startActivityForResult(intent3,CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK){//서브Activity에서 보내온 resultCode와 비교
                    String edtAddr=data.getStringExtra("editAddr");
                    tv_Member_list.setText(edtAddr);
                    str_project_member = edtAddr;
                }else{

                }
                break;
        }
    }

    private void update_team(){
        final String[] array = str_project_member.split(",");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share").child("id_list");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String strname = (String) fileSnapshot.child("name").getValue();
                    Log.v("TAG: value is ", strname);
                    for(int i=0; i<array.length;i++) {
                        if (strname.equals(array[i])) {
                            String strteam = (String) fileSnapshot.child("team").getValue();
                            String in = strteam + "," + str_project_name;
                            mRootRef.child("Schedule_Share").child("id_list").child(strname).child("team").setValue(in);
                        }
                    }
                }
                databaseRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });


    }

    public void postFirebaseDatabase(boolean add){
        str_project_name = et_input_name.getText().toString();
        str_project_info = et_input_goal.getText().toString();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(str_project_name, str_project_info, project_date,str_project_member,str_project_notice);
            postValues = post.toScheduleMap();
        }
        childUpdates.put("Schedule_Share"+"/project_list/" + str_project_name, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    //두 날짜 비교하여 몇일인지 계산하는 함수(년.월.일 -> - - - 표시)
    private void calDate(){
        str_date1 = tv_start_date.getText().toString();
        str_date2 = tv_finish_date.getText().toString();

        try{

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date StartDate = format.parse(str_date1);
            Date FinishDate = format.parse(str_date2);

            long calDate = FinishDate.getTime() - StartDate.getTime();
            calDateDays = calDate / (24*60*60*1000);
            calDateDays = Math.abs(calDateDays);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void findviewlist(){
        tv_name = findViewById(R.id.name);
        tv_period = findViewById(R.id.period);
        tv_member = findViewById(R.id.member);
        tv_input_period = findViewById(R.id.input_period);
        tv_memeber_num = findViewById(R.id.memeber_num);
        tv_Member_list = findViewById(R.id.member_list);
        tv_start = findViewById(R.id.start);
        tv_finish = findViewById(R.id.finish);
        tv_start_date = findViewById(R.id.start_date);
        tv_finish_date = findViewById(R.id.finish_date);
        tv_period_1 = findViewById(R.id.period_1);
        et_input_name = findViewById(R.id.input_name);
        et_input_goal = findViewById(R.id.input_goal);
        btn_Add_member = findViewById(R.id.add_member);
        btn_done = findViewById(R.id.done);
        btn_start_button = findViewById(R.id.start_button);
        btn_finish_button = findViewById(R.id.finish_button);
    }

    private void onclicklist(){
        btn_Add_member.setOnClickListener(this);
        btn_done.setOnClickListener(this);
    }

}