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

public class QuizmakerActivity extends AppCompatActivity {

    RecyclerView quizmakerRecyclerViews;
    RecyclerView.LayoutManager layoutManager;

    private Button btn_quizmaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmaker2);

        quizmakerRecyclerViews = findViewById(R.id.quizmaker_rv);
        quizmakerRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        quizmakerRecyclerViews.setLayoutManager(layoutManager);

        ArrayList<Quizmaker> quizmakerArrayList = new ArrayList<>();

        quizmakerArrayList.add(new Quizmaker("문제1 :"));
        quizmakerArrayList.add(new Quizmaker("문제2 :"));
        quizmakerArrayList.add(new Quizmaker("문제3 :"));
        quizmakerArrayList.add(new Quizmaker("문제4 :"));

        QuizmakerRVAdapter quizmakerRVAdapter = new QuizmakerRVAdapter(quizmakerArrayList);
        quizmakerRecyclerViews.setAdapter(quizmakerRVAdapter);

        btn_quizmaker = findViewById(R.id.btnQuizmake);
        btn_quizmaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 퀴즈의 문제와 정답 보기를 DB에 저장

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