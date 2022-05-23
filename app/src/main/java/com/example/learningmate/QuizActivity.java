package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    RecyclerView quizRecyclerViews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizRecyclerViews = findViewById(R.id.quiz_rv);
        quizRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        quizRecyclerViews.setLayoutManager(layoutManager);

        ArrayList<Quiz> quizArrayList = new ArrayList<>();

        quizArrayList.add(new Quiz("과제1"));
        quizArrayList.add(new Quiz("과제2"));
        quizArrayList.add(new Quiz("과제3"));

        QuizRVAdapter quizRVAdapter = new QuizRVAdapter(quizArrayList);
        quizRecyclerViews.setAdapter(quizRVAdapter);


        quizRVAdapter.setOnItemClickListener(new QuizRVAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ProblemActivity.class);
                startActivity(intent);
            }
        });
    }
}
