package com.example.learningmate.mentee_activity;

import android.os.Bundle;

import com.example.learningmate.R;
import com.example.learningmate.adapter.FileRVAdapter;
import com.example.learningmate.model.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {

    RecyclerView fileRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        fileRecyclerView = findViewById(R.id.file_rv);
        fileRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        fileRecyclerView.setLayoutManager(layoutManager);

        ArrayList<File> fileArrayList = new ArrayList<>();

//        fileArrayList.add(new File("과제1", "제출 완료", 0, 22, 5, 27, 10));

//        FileRVAdapter fileRVAdapter = new FileRVAdapter(fileArrayList);
//        fileRecyclerView.setAdapter(fileRVAdapter);


    }
}