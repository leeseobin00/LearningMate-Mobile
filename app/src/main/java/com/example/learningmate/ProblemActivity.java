package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProblemActivity extends AppCompatActivity {

    RecyclerView problemRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        problemRecyclerView  = findViewById(R.id.problem_rv);
        problemRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        problemRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Problem> problemArrayList = new ArrayList<>();

        problemArrayList.add(new Problem("문제1 :", "1","2","3","4"));
        problemArrayList.add(new Problem("문제2 :", "1","2","3","4"));
        problemArrayList.add(new Problem("문제3 :", "1","2","3","4"));
        problemArrayList.add(new Problem("문제4 :", "1","2","3","4"));

        ProblemRVAdapter problemRVAdapter = new ProblemRVAdapter(problemArrayList);
        problemRecyclerView.setAdapter(problemRVAdapter);

        btn_submit = findViewById(R.id.quizSubmit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB에 저장된 정답과 멘티가 입력한 정답과 비교하여 점수 계산

                // 퀴즈 목록으로 이동함
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(intent);
            }
        });
        ImageButton back_ib = findViewById(R.id.back_ib);
        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
