package com.example.learningmate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.learningmate.common_activity.SettingsActivity;
import com.example.learningmate.mentee_activity.AlertActivity;
import com.example.learningmate.mentee_activity.HomeworkActivity;
import com.example.learningmate.mentee_activity.LibraryActivity;
import com.example.learningmate.mentee_activity.QuizActivity;
import com.example.learningmate.mentor_activity.AlertMentorActivity;
import com.example.learningmate.mentor_activity.HomeworkMentorActivity;
import com.example.learningmate.mentor_activity.LibraryMentorActivity;
import com.example.learningmate.mentor_activity.QuizMentorActivity;
import com.example.learningmate.model.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
//        Toast.makeText(mContext, "현재 유저 id: " + User.currentUser.getUserId(), Toast.LENGTH_SHORT).show();
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
        CardView librarycv = findViewById(R.id.home_library_cv);


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



        usercv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.currentUser.getPairId().equals("null")) {
                    alertDialogForNotRegister();
                } else {
                    if(User.currentUser.getIdentity() == 0){
                        Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), AlertMentorActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });

        //퀴즈 눌렀을 때 퀴즈Activity로 이동
        quizcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.currentUser.getPairId().equals("null")) {
                    alertDialogForNotRegister();
                } else {
                    if (User.currentUser.getIdentity() == 0) {
                        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), QuizMentorActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


        homeworkcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.currentUser.getPairId().equals("null")) {
                    alertDialogForNotRegister();
                } else {
                    if (User.currentUser.getIdentity() == 0) {
                        Intent intent = new Intent(getApplicationContext(), HomeworkActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), HomeworkMentorActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        librarycv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.currentUser.getPairId().equals("null")) {
                    alertDialogForNotRegister();
                } else {
                    if (User.currentUser.getIdentity() == 0) {
                        Intent intent = new Intent(getApplicationContext(), LibraryActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LibraryMentorActivity.class);
                        startActivity(intent);
                    }
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

    public void alertDialogForNotRegister() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("접근 불가")
                .setMessage("등록된 " + (User.currentUser.getIdentity() == 0 ? "멘토" : "멘티") + "가 없습니다!")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}