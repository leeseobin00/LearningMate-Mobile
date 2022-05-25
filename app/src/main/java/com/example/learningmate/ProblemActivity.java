package com.example.learningmate;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ProblemActivity extends AppCompatActivity {

    RecyclerView problemRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
/*
        btn_submit = findViewById(R.id.btnQuizSubmit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB에 저장된 정답과 멘티가 입력한 정답과 비교하여 점수 계산

                // 퀴즈 목록으로 이동함
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(intent);
            }
        });*/
    }
}
