package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlertMentorActivity extends AppCompatActivity {

    RecyclerView alertMentorRecyclerViews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        alertMentorRecyclerViews = findViewById(R.id.alertmentor_rv);
        alertMentorRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        alertMentorRecyclerViews.setLayoutManager(layoutManager);

        ArrayList<AlertMentor> alertMentorArrayList = new ArrayList<>();

        alertMentorArrayList.add(new AlertMentor("알람1", "제출 완료"));
        alertMentorArrayList.add(new AlertMentor("알람2", "미제출"));
        alertMentorArrayList.add(new AlertMentor("알람3", "제출 완료"));

        AlertMentorRVAdapter alertMentorRVAdapter = new AlertMentorRVAdapter(alertMentorArrayList);
        alertMentorRecyclerViews.setAdapter(alertMentorRVAdapter);
        ImageButton back_ib = findViewById(R.id.back_ib);
        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}