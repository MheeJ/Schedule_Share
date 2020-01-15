package com.example.schedule_share;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private FirebaseUser current;
    private static final String TAG = "EmailPassword";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]*$");
    private DatePickerDialog.OnDateSetListener callbackMethod;

    private Button btn_Insert, btn_Check_Name, btn_Birth;
    private TextView birthday;
    private EditText edit_ID,edit_PW, edit_PW_Check, edit_Birthday, edit_Name, edit_Age;
    private CheckBox check_Man, check_Woman;

    private String ID, PW, checkPW, name, BIRTHDAY;
    private String team = "";
    private String log_name="No";
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findviewlist();
        onclicklist();
        initObject();

        mAuth = FirebaseAuth.getInstance();

        InitializeListener();
        InitializeView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                ID = edit_ID.getText().toString().trim();
                PW = edit_PW.getText().toString().trim();
                checkPW = edit_PW_Check.getText().toString().trim();
                //name = edit_Name.getText().toString().trim();
                BIRTHDAY = edit_Birthday.getText().toString().trim();
                createAccount();
                edit_ID.requestFocus();
                edit_ID.setCursorVisible(true);
                break;

            case R.id.check_name:
                name = edit_Name.getText().toString().trim();
                log_name = "Yes";
                getdata();
                break;

            case R.id.check_man:
                check_Woman.setChecked(false);
                gender = "Man";
                break;

            case R.id.check_woman:
                check_Man.setChecked(false);
                gender = "Woman";
                break;

            case R.id.btn_login:
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
                break;

            case R.id.btn_birth:
                InitializeView();
                InitializeListener();
                DatePickerDialog dialog = new DatePickerDialog(this,callbackMethod,2020,1,12);
                dialog.show();
                break;
        }
    }

    private void findviewlist() {
        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Check_Name = (Button) findViewById(R.id.check_name);
        btn_Birth = (Button)findViewById(R.id.btn_birth);
        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_PW = (EditText) findViewById(R.id.edit_pw);
        edit_PW_Check = (EditText)findViewById(R.id.pw_check);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        edit_Birthday = (EditText) findViewById(R.id.birthday);
        check_Man = (CheckBox) findViewById(R.id.check_man);
        check_Woman = (CheckBox) findViewById(R.id.check_woman);
    }

    private void onclicklist() {
        btn_Insert.setOnClickListener(this);
        btn_Check_Name.setOnClickListener(this);
        btn_Birth.setOnClickListener(this);
        check_Man.setOnClickListener(this);
        check_Woman.setOnClickListener(this);
    }

    private void initObject(){
        btn_Insert.setEnabled(true);
        edit_PW.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edit_PW_Check.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void setInsertMode() {
        edit_ID.setText("");
        edit_PW.setText("");
        edit_Name.setText("");
        edit_Age.setText("");
        check_Man.setChecked(false);
        check_Woman.setChecked(false);
        btn_Insert.setEnabled(true);
    }

    private void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear+1;
                edit_Birthday.setText(year + "년"+monthOfYear+"월"+dayOfMonth+"일");
            }
        };
    }

    private void InitializeView() {
        birthday = (TextView)findViewById(R.id.birthday);
    }

    private void getdata() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share");
        databaseRef.child("id_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String strname = (String) fileSnapshot.child("name").getValue();
                    Log.v("TAG: value is ", strname);
                    if(strname.equals(name)){
                        startToast("동일한 이름이 있습니다.");
                        log_name = "No";
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }

    private void postDatabase(boolean add) {
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            FirebasePost post = new FirebasePost(ID, PW, name, BIRTHDAY, gender,team);
            postValues = post.toMap();
        }
        childUpdates.put("Schedule_Share"+"/id_list/" + name, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    private void createAccount() {
        String email = ID;
        String password = PW;

        if (email.length() > 0 && password.length() > 0 && checkPW.length() > 0 && name.length() > 0 && BIRTHDAY.length()>0) {
            if (log_name.equals("Yes")) {
                if (PW.equals(checkPW)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        postDatabase(true);
                                        setInsertMode();
                                        startToast("회원가입에 성공하였습니다.");
                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);
                                        log_name ="No";
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        if (task.getException() != null) {
                                            startToast("회원가입에 실패하였습니다.");
                                        }
                                    }
                                }
                            });
                } else {
                    startToast("비밀번호가 일치하지 않습니다.");
                }
            } else {
                startToast("아이디를 확인하세요");
            }
        }else {
            startToast("회원정보를 입력하세요");
        }
    }

    private void startToast(String msg){
        Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
    }
}