package com.example.learningmate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toast.makeText(mContext, "현재 유저 id: "+User.currentUser.getUserId(), Toast.LENGTH_SHORT).show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        //Log.d("request result", postUserInfo() + "");
//            }
//        }).start();

        //카드뷰 선언
        CardView usercv = findViewById(R.id.home_user_cv);
        CardView quizcv = findViewById(R.id.home_quiz_cv);
        CardView homeworkcv = findViewById(R.id.home_homework_cv);

        //캘린더뷰
        calendarView = findViewById(R.id.home_calendar);

        //내정보
        TextView settingsView = findViewById(R.id.home_userinfo_tv);

        // 내정보 눌렀을 시에 settingActivity 이동
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });


        DateFormat formatter = new SimpleDateFormat("yyyy년 mm월 dd일");
        Date date = new Date(calendarView.getDate());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {

            }
        });

        usercv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
                startActivity(intent);
            }
        });

        //퀴즈 눌렀을 때 퀴즈Activity로 이동
        quizcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.currentUser.getIdentity() == 0) {
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), QuizMentorActivity.class);
                    startActivity(intent);
                }

            }
        });


        homeworkcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.currentUser.getIdentity() == 0) {
                    Intent intent = new Intent(getApplicationContext(), HomeworkActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), HomeworkMentorActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean postUserInfo(String id, String pw, String name, String iden) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("signin_uid", id)
                    .add("signin_password", pw)
                    .add("userName", name)
                    .add("identity", iden)
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
                    Log.d("response", body.string());
                }
            } else {
                Log.d("response", "error");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}