package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_name, et_passck;
    private Button btn_register, btn_validate;
    private RadioGroup radioGroup;
    private RadioButton btn_radio1, btn_radio2, btn_radio3;
    private AlertDialog dialog;
    private boolean validate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.editTextName);
        et_id = findViewById(R.id.editTextId);
        et_pass = findViewById(R.id.editTextPw);
        et_passck = findViewById(R.id.editTextPwCheck);

        btn_validate = findViewById(R.id.btnValidate);
        btn_register = findViewById(R.id.btnRegister2);

        radioGroup = findViewById(R.id.radioGroup);
        btn_radio1 = findViewById(R.id.radioBtn1);
        btn_radio2 = findViewById(R.id.radioBtn2);

        String id = et_id.getText().toString();
        String pw = et_pass.getText().toString();
        String name = et_name.getText().toString();

        if (btn_radio1.isChecked()) {
            ((MainActivity) MainActivity.mContext).postUserInfo(id, pw, name, "1");
        } else if (btn_radio2.isChecked()) {
            ((MainActivity) MainActivity.mContext).postUserInfo(id, pw, name, "0");
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);  // 회원가입 페이지로 이동
                startActivity(intent);
            }
        });
    }
}
