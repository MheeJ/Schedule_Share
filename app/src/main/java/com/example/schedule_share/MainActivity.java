package com.example.schedule_share;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mPostReference;

    Button Btn_Signup, Btn_Login;
    EditText UserID, UserPW;
    public String userID, userPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();



    }

    private void initView() {
        Btn_Signup = findViewById(R.id.btn_signup);
        Btn_Signup.setOnClickListener(this);
        Btn_Login = findViewById(R.id.btn_login);
        Btn_Login.setOnClickListener(this);
        UserID = (EditText) findViewById(R.id.userid);
        UserPW = (EditText) findViewById(R.id.userpw);

    }

    public void setInsertMode() {
        UserID.setText("");
        UserPW.setText("");;
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_signup :
                Intent intent1 = new Intent(this,SignUp.class);
                startActivity(intent1);
                break;
            case R.id.btn_login:
                userID = UserID.getText().toString();
                userPW = UserPW.getText().toString();
                //setInsertMode();
                Intent intent2 = new Intent(this,Login.class);
                intent2.putExtra("userID",userID);
                intent2.putExtra("userPW",userPW);

                startActivity(intent2);

        }

    }
}