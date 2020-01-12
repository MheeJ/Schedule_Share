package com.example.schedule_share;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    Button btn_Insert,Check_Name,Btn_Birth;
    TextView birthday;
    EditText edit_ID;
    EditText edit_PW;
    EditText PW_Check, mETBirthday;
    EditText edit_Name;
    EditText edit_Age;
    CheckBox check_Man;
    CheckBox check_Woman;
   // private TextView birthday;
    int count;
    String strname;
    String ID;
    String PW;
    String checkPW;
    String name;
    String log_name="No";
    String BIRTHDAY;
    long age;
    String gender = "";
    String sort = "id";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        initObject();
        mAuth = FirebaseAuth.getInstance();

        InitializeListener();
        InitializeView();
    }

    public void initObject(){
        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        Check_Name = (Button) findViewById(R.id.check_name);
        Check_Name.setOnClickListener(this);
        Btn_Birth = (Button)findViewById(R.id.btn_birth);
        Btn_Birth.setOnClickListener(this);
        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_PW = (EditText) findViewById(R.id.edit_pw);
        PW_Check = (EditText)findViewById(R.id.pw_check);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        mETBirthday = (EditText) findViewById(R.id.birthday);
        check_Man = (CheckBox) findViewById(R.id.check_man);
        check_Man.setOnClickListener(this);
        check_Woman = (CheckBox) findViewById(R.id.check_woman);
        check_Woman.setOnClickListener(this);
        btn_Insert.setEnabled(true);
        edit_PW.setTransformationMethod(PasswordTransformationMethod.getInstance());
        PW_Check.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void setInsertMode() {
        edit_ID.setText("");
        edit_PW.setText("");
        edit_Name.setText("");
        edit_Age.setText("");
        check_Man.setChecked(false);
        check_Woman.setChecked(false);
        btn_Insert.setEnabled(true);

    }

    //생일선택달력
    private void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear+1;
                mETBirthday.setText(year + "년"+monthOfYear+"월"+dayOfMonth+"일");
            }
        };
    }

    private void InitializeView() {
        birthday = (TextView)findViewById(R.id.birthday);
    }

 /*   public boolean IsExistID() {
        boolean IsExist = arrayIndex.contains(name);
        return IsExist;
    }*/


    public void getdata() {
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
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

    public void postFirebaseDatabase(boolean add) {
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            FirebasePost post = new FirebasePost(ID, PW, name, BIRTHDAY, gender);
            postValues = post.toMap();
        }
        childUpdates.put("Schedule_Share"+"/id_list/" + name, postValues);
        mPostReference.updateChildren(childUpdates);
    }




/*
    public String setTextLength(String text, int length) {
        if (text.length() < length) {
            int gap = length - text.length();
            for (int i = 0; i < gap; i++) {
                text = text + " ";
            }
        }
        return text;
    }*/




    private void createAccount() {
        // [START create_user_with_email]
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
                                        postFirebaseDatabase(true);
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

 /*   public void createDatabase(){
        if (!IsExistID()) {
            postFirebaseDatabase(true);
            setInsertMode();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SignUp.this, "이미 존재하는 이름 입니다. 다른 이름으로 설정해주세요.", Toast.LENGTH_LONG).show();
        }
        edit_ID.requestFocus();
        edit_ID.setCursorVisible(true);
    }

*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                ID = edit_ID.getText().toString().trim();
                PW = edit_PW.getText().toString().trim();
                checkPW = PW_Check.getText().toString().trim();
                //name = edit_Name.getText().toString().trim();
                BIRTHDAY = mETBirthday.getText().toString().trim();
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


}