package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.learningmate.adapter.QuizRVAdapter;
import com.example.learningmate.mentee_activity.ProblemActivity;
import com.example.learningmate.mentee_activity.QuizActivity;
import com.example.learningmate.model.Quiz;
import com.example.learningmate.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.ArrayList;

public class AlertMentorActivity extends AppCompatActivity {

    RecyclerView alertMentorRecyclerViews;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<AlertMentor> alertMentorArrayList;
    AlertMentorRVAdapter alertMentorRVAdapter;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg2 == 1) {
                alertMentorRVAdapter = new AlertMentorRVAdapter(alertMentorArrayList);
                alertMentorRecyclerViews.setAdapter(alertMentorRVAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_mentor);

        alertMentorRecyclerViews = findViewById(R.id.alertmentor_rv);
        alertMentorRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        alertMentorRecyclerViews.setLayoutManager(layoutManager);
        alertMentorArrayList = new ArrayList<>();
//        ArrayList<AlertMentor> alertMentorArrayList = new ArrayList<>();
//
//        alertMentorArrayList.add(new AlertMentor("알람1", "제출 완료"));
//        alertMentorArrayList.add(new AlertMentor("알람2", "미제출"));
//        alertMentorArrayList.add(new AlertMentor("알람3", "제출 완료"));
//
//        AlertMentorRVAdapter alertMentorRVAdapter = new AlertMentorRVAdapter(alertMentorArrayList);
//        alertMentorRecyclerViews.setAdapter(alertMentorRVAdapter);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSubmissionList();
            }
        }).start();
    }

    public void getSubmissionList() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/assignment_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("alert_mentor", User.currentUser.getUserId())
                    .build();

            Request request = new Request
                    .Builder()
                    .url(url)
                    .post(formBody).build();

            Response response = client
                    .newCall(request)
                    .execute();

            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    String data = body.string();
                    Log.d("data", data);
                    if (data.equals("None")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AlertMentorActivity.this, "활동 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        alertMentorArrayList.clear();
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            alertMentorArrayList.add(new AlertMentor(jsonObject.optString("title")
                                    , jsonObject.optString("date")));
                        }
                        Message message = handler.obtainMessage();
                        message.arg2 = 1;
                        handler.sendMessage(message);
                    }
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}