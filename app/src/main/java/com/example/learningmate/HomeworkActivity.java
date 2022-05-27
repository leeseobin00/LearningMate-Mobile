package com.example.learningmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

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

public class HomeworkActivity extends AppCompatActivity {

    RecyclerView homeworkRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Homework> homeworkArrayList;
    private HomeworkRVAdapter homeworkRVAdapter;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 1) {
                homeworkRVAdapter = new HomeworkRVAdapter(homeworkArrayList);
                homeworkRecyclerView.setAdapter(homeworkRVAdapter);
                return;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        homeworkRecyclerView = findViewById(R.id.homework_rv);
        homeworkRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        homeworkRecyclerView.setLayoutManager(layoutManager);

        homeworkArrayList = new ArrayList<>();

//        homeworkArrayList.add(new Homework("과제1", 22, 5, 27, 10));
//        homeworkArrayList.add(new Homework("과제2", 22, 5, 27, 10));
//        homeworkArrayList.add(new Homework("과제3", 22, 5, 27, 10));




        //file업로드 액티비티 만들면 연결
        homeworkRVAdapter.setOnItemClickListener(new HomeworkRVAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), HomeworkDetailActivity.class);
                startActivity(intent);
            }

        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                getHomeworkList();
            }
        }).start();

    }

    public void getHomeworkList() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/assignment_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("user", User.currentUser.getUserId())
                    .add("identity", User.currentUser.getIdentity() + "")
                    .add("pair", User.currentUser.getPairId())
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
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        homeworkArrayList.add(new Homework(jsonObject.get("assign_id").toString()
                                , jsonObject.get("uploader").toString()
                                , jsonObject.get("assign_data").toString()
                                , jsonObject.get("assignment_title").toString()
                                , jsonObject.get("assignment_body").toString()
                                , Integer.parseInt(jsonObject.get("graded_score").toString())
                                , Integer.parseInt(jsonObject.get("perfect_score").toString())
                                , Integer.parseInt(jsonObject.get("file_id").toString())
                                , jsonObject.get("due_date").toString()));
                    }
                    Message message = handler.obtainMessage();
                    message.arg1 = 1;
                    handler.sendMessage(message);
                    Log.d("data", data);
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}