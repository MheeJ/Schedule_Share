package com.example.schedule_share;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Make_project extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mPostReference;

    private TextView name, period, goal, member,start,finish, start_date,finish_date,input_period,memeber_num,period_1,Member_list;
    private EditText input_name, input_goal,input_member;
    private Button done,start_button,finish_button,Add_member;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private DatePickerDialog.OnDateSetListener callbackMethod2;
    String getData="";
    final static int CODE=1;

    private String project_name,date1,date2,Days;
    private String project_info;
    String project_member="멤버";
    private long project_date,calDateDays,pdays;

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_project);

        initView();
        this.InitializeListener();

        this.InitializeListener2();


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);




    }

    private void initView(){
        name = findViewById(R.id.name);
        period = findViewById(R.id.period);
        member = findViewById(R.id.member);
        input_period = findViewById(R.id.input_period);
        input_name = findViewById(R.id.input_name);
        input_goal = findViewById(R.id.input_goal);
        memeber_num = findViewById(R.id.memeber_num);
        Add_member = findViewById(R.id.add_member);
        Member_list = findViewById(R.id.member_list);
        start = findViewById(R.id.start);
        finish = findViewById(R.id.finish);
        start_date = findViewById(R.id.start_date);
        finish_date = findViewById(R.id.finish_date);
        done = findViewById(R.id.done);
        period_1 = findViewById(R.id.period_1);
        start_button = findViewById(R.id.start_button);
        finish_button = findViewById(R.id.finish_button);
        Add_member.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    public void InitializeListener(){
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                start_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
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
                finish_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);

                calDate();

              long i = calDateDays/7;
              project_date = i;
              Days = Long.toString(i) ;
              input_period.setText(Days);
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
                String real_name = input_name.getText().toString();
                Project_info projectInfo = new Project_info();
                Intent intent2 = new Intent(this,Project_list.class);
                intent2.putExtra("name", projectInfo);
                startActivity(intent2);
                postFirebaseDatabase(true);
                break;

            case R.id.add_member:
                Intent intent3 = new Intent(this,Add_User.class);
                intent3.putExtra("addr",Member_list.getText().toString());
                startActivityForResult(intent3,CODE);
                break;
          /*  case R.id.check_member:
                getData = getIntent().getStringExtra("adapter_list");
                Member_list.setText(getData);
                getData = project_member;
                break;*/
        }
    }

    //서브액티비티가 값을 보내오면 자동호출되는 메소드
    //requestCode에 메인 액티비티에서 보낸 CODE가 담겨온다.
    @Override
    //onActivityResult( 메인액티비티에서 보낸 코드,서브액티비티에서 보내온 리절트코드,서브액티비티에서 데이터를 담아 보낸 인텐트 )
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK){//서브Activity에서 보내온 resultCode와 비교
                    //서브액티비티에서 인텐트에 담아온 정보 꺼내기
                    String edtAddr=data.getStringExtra("editAddr");
                    //텍스트뷰에 수정된 문자열 넣기
                    Member_list.setText(edtAddr);
                    project_member = edtAddr;
                }else{

                }
                break;
        }
    }

    public void postFirebaseDatabase(boolean add){
        project_name = input_name.getText().toString();
        project_info = input_goal.getText().toString();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(project_name, project_info, project_date,project_member);
            postValues = post.toScheduleMap();
        }
        childUpdates.put("/project_list/" + project_name, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    //두 날짜 비교하여 몇일인지 계산하는 함수(년.월.일 -> - - - 표시)
    public void calDate(){
        date1 = start_date.getText().toString();
        date2 = finish_date.getText().toString();

        try{

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date StartDate = format.parse(date1);
            Date FinishDate = format.parse(date2);

            long calDate = FinishDate.getTime() - StartDate.getTime();
            calDateDays = calDate / (24*60*60*1000);
            calDateDays = Math.abs(calDateDays);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
