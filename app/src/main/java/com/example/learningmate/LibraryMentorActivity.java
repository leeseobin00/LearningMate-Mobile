package com.example.learningmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class LibraryMentorActivity extends AppCompatActivity {
    ImageButton addButton;
    ImageButton removeButton;
    RecyclerView libraryRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Library> libraryArrayList;
    private LibraryRVAdapter libraryRVAdapter;

    private String libraryName;
    private String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_mentor);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        libraryRecyclerView = findViewById(R.id.library_mentor_rv);

        libraryRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        libraryRecyclerView.setLayoutManager(layoutManager);
        libraryArrayList = new ArrayList<>();

        LibraryRVAdapter libraryRVAdapter = new LibraryRVAdapter(libraryArrayList);
        libraryRecyclerView.setAdapter(libraryRVAdapter);

        addButton = findViewById(R.id.library_add_ib);
        removeButton = findViewById(R.id.library_remove_ib);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                getLibraryList();
            }
        }).start();*/
    }


    public void getLibraryList(){

    }
}