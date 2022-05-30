package com.example.learningmate.mentor_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.learningmate.R;
import com.example.learningmate.adapter.HomeworkRVAdapter;
import com.example.learningmate.model.Homework;
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

public class HomeworkMentorActivity extends AppCompatActivity {

    ImageButton addButton;
    ImageButton removeButton;
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
                homeworkRVAdapter.setOnItemClickListener(new HomeworkRVAdapter.OnItemClickListener(){

                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(getApplicationContext(), HomeworkDetailMentorActivity.class);
                        intent.putExtra("data", homeworkArrayList.get(position));
                        startActivity(intent);
                    }

                });
                return;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_mentor);
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        homeworkRecyclerView = findViewById(R.id.homework_mentor_rv);

        homeworkRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        homeworkRecyclerView.setLayoutManager(layoutManager);
        homeworkArrayList = new ArrayList<>();

        addButton = findViewById(R.id.homework_add_ib);
        removeButton = findViewById(R.id.homework_remove_ib);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getHomeworkList();
            }
        }).start();
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


    public void getHomeworkList() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/assignment_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("mUser", User.currentUser.getUserId())
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
                    Log.d("data", data);
                    if(data.contains("No Assignment")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeworkMentorActivity.this, "과제가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(data);
                    homeworkArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        homeworkArrayList.add(new Homework(jsonObject.get("assign_id").toString()
                                , jsonObject.get("uploader").toString()
                                , jsonObject.get("assign_date").toString()
                                , jsonObject.get("assignment_title").toString()
                                , jsonObject.get("assignment_body").toString()
                                , Integer.parseInt(jsonObject.get("graded_score").toString().equals("null") ? "-1" : jsonObject.get("graded_score").toString())
                                , Integer.parseInt(jsonObject.get("perfect_score").toString())
                                , Integer.parseInt(jsonObject.get("file_id").toString())
                                , jsonObject.get("submit_id").toString()
                                , jsonObject.get("due_date").toString()));
                    }
                    Message message = handler.obtainMessage();
                    message.arg1 = 1;
                    handler.sendMessage(message);
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getHomeworkList();
            }
        }).start();
    }
}
