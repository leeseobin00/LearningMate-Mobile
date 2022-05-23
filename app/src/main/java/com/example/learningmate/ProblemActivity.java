package com.example.learningmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProblemActivity extends AppCompatActivity {

    RecyclerView problemRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        int result = 0;

        problemRecyclerView = findViewById(R.id.problem_rv);
        problemRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        problemRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Problem> problemArrayList = new ArrayList<>();

        problemArrayList.add(new Problem("Problem 1", "answer1", "answer2", "answer3", "answer4"));
        problemArrayList.add(new Problem("Problem 1", "answer1", "answer2", "answer3", "answer4"));
        problemArrayList.add(new Problem("Problem 1", "answer1", "answer2", "answer3", "answer4"));
        problemArrayList.add(new Problem("Problem 1", "answer1", "answer2", "answer3", "answer4"));

        ProblemRVAdapter problemRVAdapter = new ProblemRVAdapter(problemArrayList);
        problemRecyclerView.setAdapter(problemRVAdapter);

//        Button btn_next1 = (Button) findViewById(R.id.q1btnNext);

        //btn_next1.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent intent = new Intent(getApplicationContext(), MainActivity.class);    // quiz2로 이동하는 것으로 바꿔야함
        //        startActivity(intent);
        //    }
        //});
    }
}
