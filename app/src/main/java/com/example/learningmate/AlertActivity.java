package com.example.learningmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity {

    RecyclerView alertRecyclerViews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        alertRecyclerViews = findViewById(R.id.alert_rv);
        alertRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        alertRecyclerViews.setLayoutManager(layoutManager);

        ArrayList<Alert> alertArrayList = new ArrayList<>();

        alertArrayList.add(new Alert("알람1", 22, 5, 27, "제출 완료"));
        alertArrayList.add(new Alert("알람2", 22, 5, 27, "미제출"));
        alertArrayList.add(new Alert("알람3", 22, 5, 27, "제출 완료"));

        AlertRVAdapter alertRVAdapter = new AlertRVAdapter(alertArrayList);
        alertRecyclerViews.setAdapter(alertRVAdapter);
    }
}