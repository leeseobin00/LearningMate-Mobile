package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login,btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";

        et_id=findViewById(R.id.editTextId);
        et_pass=findViewById(R.id.editTextPw);
        btn_login=findViewById(R.id.btnLogin);
        btn_register=findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
            }
        });
    }
}