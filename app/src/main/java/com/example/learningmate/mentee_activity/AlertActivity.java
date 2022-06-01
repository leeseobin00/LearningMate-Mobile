package com.example.learningmate.mentee_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.learningmate.adapter.AlertRVAdapter;
import com.example.learningmate.MainActivity;
import com.example.learningmate.R;
import com.example.learningmate.model.Alert;
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
import java.util.Collections;

public class AlertActivity extends AppCompatActivity {

    RecyclerView alertRecyclerViews;
    RecyclerView.LayoutManager layoutManager;
    AlertRVAdapter alertRVAdapter;
    ArrayList<Alert> alertArrayList;
    int checkSum = 0;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            checkSum += msg.arg1;
            if (checkSum == 2) {
                Collections.sort(alertArrayList, Collections.reverseOrder());
                alertRVAdapter = new AlertRVAdapter(alertArrayList);
                alertRecyclerViews.setAdapter(alertRVAdapter);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        alertRecyclerViews = findViewById(R.id.alert_rv);
        alertRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        alertRecyclerViews.setLayoutManager(layoutManager);

        alertArrayList = new ArrayList<>();

//        alertArrayList.add(new Alert("알람1", 22, 5, 27, "제출 완료"));
//        alertArrayList.add(new Alert("알람2", 22, 5, 27, "미제출"));
//        alertArrayList.add(new Alert("알람3", 22, 5, 27, "제출 완료"));

//        AlertRVAdapter alertRVAdapter = new AlertRVAdapter(alertArrayList);
//        alertRecyclerViews.setAdapter(alertRVAdapter);
        ImageButton back_ib = findViewById(R.id.back_ib);
        back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAssignmentList();
                getMaterialList();

            }
        }).start();

    }

    public void getAssignmentList() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/assignment_controller.php";
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("alert_mentor_for_mentee", User.currentUser.getPairId())
                    .add("state", "0")
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
                                Toast.makeText(AlertActivity.this, "자료가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            alertArrayList.add(new Alert(jsonObject.optString("title")
                                    , jsonObject.optString("date")
                                    , jsonObject.optString("due")
                                    , jsonObject.optString("submit").equals("null") ? false : true
                                    , 0
                            ));
                        }
                        Message message = handler.obtainMessage();
                        message.arg1 = 1;
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

    public void getMaterialList() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/assignment_controller.php";
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("alert_mentor_for_mentee", User.currentUser.getPairId())
                    .add("state", "1")
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
                                Toast.makeText(AlertActivity.this, "자료가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            alertArrayList.add(new Alert(jsonObject.optString("title")
                                    , jsonObject.optString("date")
                                    , 1
                            ));
                        }
                        Message message = handler.obtainMessage();
                        message.arg1 = 1;
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