package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeworkMentorActivity extends AppCompatActivity {

    ImageButton addButton;
    ImageButton removeButton;

    RecyclerView homeworkRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    HomeworkRVAdapter homeworkRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_mentor);

        homeworkRecyclerView = findViewById(R.id.homework_mentor_rv);
        homeworkRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        homeworkRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Homework> homeworkArrayList = new ArrayList<>();

        homeworkArrayList.add(new Homework("과제1", 22, 5, 27, 10));
        homeworkArrayList.add(new Homework("과제2", 22, 5, 27, 10));
        homeworkArrayList.add(new Homework("과제3", 22, 5, 27, 10));

        homeworkRVAdapter = new HomeworkRVAdapter(homeworkArrayList);
        homeworkRecyclerView.setAdapter(homeworkRVAdapter);

        addButton = findViewById(R.id.homework_add_ib);
        removeButton = findViewById(R.id.homework_remove_ib);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FileMentorActivity.class);
                startActivity(intent);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeworkRVAdapter.removeItem(homeworkRVAdapter.getPosition());

            }
        });
    }
}
