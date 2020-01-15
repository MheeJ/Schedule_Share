package com.example.schedule_share;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button Btn_Signup, Btn_Login;
    private EditText Et_userID, Et_userPW;
    public String str_userID, str_userPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviewlist();
        onclicklist();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signup :
                Intent intent1 = new Intent(this,SignUp.class);
                startActivity(intent1);
                break;

            case R.id.btn_login:
                str_userID = Et_userID.getText().toString();
                str_userPW = Et_userPW.getText().toString();
                //setInsertMode();
                Intent intent2 = new Intent(this,Login.class);
                intent2.putExtra("userID", str_userID);
                intent2.putExtra("userPW",str_userPW);
                startActivity(intent2);
        }
    }

    private void findviewlist() {
        Btn_Signup = findViewById(R.id.btn_signup);
        Btn_Login = findViewById(R.id.btn_login);
        Et_userID = findViewById(R.id.userid);
        Et_userPW = findViewById(R.id.userpw);
    }

    private void onclicklist(){
        Btn_Signup.setOnClickListener(this);
        Btn_Login.setOnClickListener(this);
    }
}