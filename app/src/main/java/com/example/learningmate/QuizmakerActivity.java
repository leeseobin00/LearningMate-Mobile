package com.example.learningmate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class QuizmakerActivity extends AppCompatActivity {

    RecyclerView quizmakerRecyclerViews;
    RecyclerView.LayoutManager layoutManager;

    private Button createQuizButton;
    private EditText inputTitle;
    private EditText inputTimeLimit;
    private int totalScore = 0;
    private int createdQuizId = 0;
    private int checkSum = 0;
    private ArrayList<QuizMaker> quizMakerArrayList;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            checkSum += msg.arg2;
            if (checkSum == 4) {
                Toast.makeText(QuizmakerActivity.this, "퀴즈 등록 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (msg.arg1 == 1) {

                for (int i = 0; i < quizMakerArrayList.size(); ++i) {
                    int cur = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            createDetailQuizContent(createdQuizId
                                    , quizMakerArrayList.get(cur).getQuestion()
                                    , quizMakerArrayList.get(cur).getPoint()
                                    , quizMakerArrayList.get(cur).getChoice1()
                                    , quizMakerArrayList.get(cur).getChoice2()
                                    , quizMakerArrayList.get(cur).getChoice3()
                                    , quizMakerArrayList.get(cur).getChoice4()
                                    , quizMakerArrayList.get(cur).getAnswer()
                            );
                        }
                    }).start();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmaker2);

        quizmakerRecyclerViews = findViewById(R.id.quizmaker_rv);
        inputTitle = findViewById(R.id.quiz_name_et);
        inputTimeLimit = findViewById(R.id.time_limit);
        quizmakerRecyclerViews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        quizmakerRecyclerViews.setLayoutManager(layoutManager);

        quizMakerArrayList = new ArrayList<>();

        quizMakerArrayList.add(new QuizMaker("문제 1"));
        quizMakerArrayList.add(new QuizMaker("문제 2"));
        quizMakerArrayList.add(new QuizMaker("문제 3"));
        quizMakerArrayList.add(new QuizMaker("문제 4"));

        QuizmakerRVAdapter quizmakerRVAdapter = new QuizmakerRVAdapter(quizMakerArrayList);
        quizmakerRecyclerViews.setAdapter(quizmakerRVAdapter);

        createQuizButton = findViewById(R.id.btnQuizmake);
        createQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 퀴즈의 문제와 정답 보기를 DB에 저장
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createQuizRequest();
                    }
                }).start();
            }
        });
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void createQuizRequest() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/quiz_controller.php";
            OkHttpClient client = new OkHttpClient();
            for(int i=0; i<quizMakerArrayList.size(); ++i){
                totalScore += Integer.parseInt(quizMakerArrayList.get(i).getPoint());
            }
            RequestBody formBody = new FormBody.Builder()
                    .add("create_uid", User.currentUser.getUserId())
                    .add("create_title", inputTitle.getText().toString())
                    .add("time_limit", inputTimeLimit.getText().toString())
                    .add("perfect_score", totalScore + "")
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
                    if (data.equals("Fail")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(QuizmakerActivity.this, "퀴즈 생성에 실패했습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        createdQuizId = Integer.parseInt(data);
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

    public void createDetailQuizContent(int id, String question, String point, String c1, String c2, String c3, String c4, String answer) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/quiz_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("quiz_id", id + "")
                    .add("question", question)
                    .add("point", point + "")
                    .add("choice1", c1)
                    .add("choice2", c2)
                    .add("choice3", c3)
                    .add("choice4", c4)
                    .add("answer", answer)
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
                    if (data.equals("0")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(QuizmakerActivity.this, "퀴즈 생성에 실패했습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
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