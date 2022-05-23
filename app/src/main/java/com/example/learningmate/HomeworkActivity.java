package com.example.learningmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeworkActivity extends AppCompatActivity {

    RecyclerView homeworkRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        homeworkRecyclerView = findViewById(R.id.homework_rv);
        homeworkRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        homeworkRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Homework> homeworkArrayList = new ArrayList<>();

        homeworkArrayList.add(new Homework("과제1", 22, 5, 27, 10));
        homeworkArrayList.add(new Homework("과제2", 22, 5, 27, 10));
        homeworkArrayList.add(new Homework("과제3", 22, 5, 27, 10));

        HomeworkRVAdapter homeworkRVAdapter = new HomeworkRVAdapter(homeworkArrayList);
        homeworkRecyclerView.setAdapter(homeworkRVAdapter);
    }
}
