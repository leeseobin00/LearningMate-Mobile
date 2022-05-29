package com.example.learningmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView libraryRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Homework> homeworkArrayList;
    private HomeworkRVAdapter homeworkRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        libraryRecyclerView = findViewById(R.id.library_rv);
        libraryRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        libraryRecyclerView.setLayoutManager(layoutManager);

        homeworkArrayList = new ArrayList<>();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                getLibraryList();
            }
        }).start();*/
    }

    public void getLibraryList() {
    }
}