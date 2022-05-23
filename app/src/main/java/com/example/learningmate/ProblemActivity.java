package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ProblemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        int answer = 0;

        RadioGroup radioGroup = findViewById(R.id.q1RadioGroup);
        RadioButton radioBtn1 = findViewById(R.id.q1Answer1);
        RadioButton radioBtn2 = findViewById(R.id.q1Answer2);
        RadioButton radioBtn3 = findViewById(R.id.q1Answer3);
        RadioButton radioBtn4 = findViewById(R.id.q1Answer4);

        Button btn_next1 = (Button) findViewById(R.id.q1btnNext);

        // 정답이 무엇인지 판단
        if (radioBtn1.isChecked()) {
            answer = 1;
        } else if (radioBtn2.isChecked()) {
            answer = 2;
        } else if (radioBtn3.isChecked()) {
            answer = 3;
        } else if (radioBtn4.isChecked()) {
            answer = 4;
        }

        btn_next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);    // quiz2로 이동하는 것으로 바꿔야함
                startActivity(intent);
            }
        });
    }
}
