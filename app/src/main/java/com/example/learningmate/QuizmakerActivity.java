package com.example.learningmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizmakerActivity extends AppCompatActivity {

    RecyclerView quizRecyclerViews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmaker2);

        quizRecyclerViews = findViewById(R.id.quizmaker_rv);
        quizRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        quizRecyclerViews.setLayoutManager(layoutManager);

        ArrayList<Quiz> quizArrayList = new ArrayList<>();

        quizArrayList.add(new Quiz("퀴즈1"));
        quizArrayList.add(new Quiz("퀴즈2"));
        quizArrayList.add(new Quiz("퀴즈3"));

        QuizRVAdapter quizRVAdapter = new QuizRVAdapter(quizArrayList);
        quizRecyclerViews.setAdapter(quizRVAdapter);


    }
}