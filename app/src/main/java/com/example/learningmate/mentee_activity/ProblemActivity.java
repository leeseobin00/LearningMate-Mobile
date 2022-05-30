package com.example.learningmate.mentee_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningmate.R;
import com.example.learningmate.adapter.ProblemRVAdapter;
import com.example.learningmate.model.Problem;
import com.example.learningmate.model.Quiz;

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

public class ProblemActivity extends AppCompatActivity {

    RecyclerView problemRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Problem> problemArrayList;
    private Button btn_submit;
    private Quiz quiz;
    private ProblemRVAdapter problemRVAdapter;
    private Button startButton;
    private int startTime = 0;
    private TextView timer;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 2) {
                Toast.makeText(ProblemActivity.this, "퀴즈가 종료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            if (msg.arg2 == 1) {
                problemRVAdapter = new ProblemRVAdapter(problemArrayList);
                problemRecyclerView.setAdapter(problemRVAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        Intent intent = getIntent();
        if (intent != null) {
            quiz = (Quiz) intent.getSerializableExtra("data");
        }
        startButton = findViewById(R.id.start_button);
        problemRecyclerView = findViewById(R.id.problem_rv);
        problemRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        problemRecyclerView.setLayoutManager(layoutManager);
        problemArrayList = new ArrayList<>();
        btn_submit = findViewById(R.id.quizSubmit);
        timer = findViewById(R.id.timer_tv);
        startButton.setText("시작하기 [제한시간: "+quiz.getTimeLimit()+"초]");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                problemRecyclerView.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                startTime = quiz.getTimeLimit();
                timer.setText(startTime + "");
                timer.setVisibility(View.VISIBLE);
                executeTimer();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB에 저장된 정답과 멘티가 입력한 정답과 비교하여 점수 계산
                // 퀴즈 목록으로 이동함
                final int grade = calculateGrade();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateQuizScore(grade);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                getQuizList();
            }
        }).start();
    }

    private int calculateGrade() {
        int grade = 0;
        for (int i = 0; i < problemArrayList.size(); ++i) {
            if (problemArrayList.get(i).getAnswer().equals(problemArrayList.get(i).getSelectAnswer())) {
                grade += problemArrayList.get(i).getPoint();
            }
        }
        return grade;
    }

    public void getQuizList() {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/quiz_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("quiz_id_detail", quiz.getQuizId())
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
                                Toast.makeText(ProblemActivity.this, "퀴즈가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            problemArrayList.add(new Problem(jsonObject.optString("question")
                                    , jsonObject.optString("choice1")
                                    , jsonObject.optString("choice2")
                                    , jsonObject.optString("choice3")
                                    , jsonObject.optString("choice4")
                                    , Integer.parseInt(jsonObject.optString("point"))
                                    , jsonObject.optString("answer")));
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

    public void updateQuizScore(int score) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/quiz_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("update_grade_quiz_id", quiz.getQuizId())
                    .add("update_grade_score", score + "")
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
                                Toast.makeText(ProblemActivity.this, "점수 반영 실패, 재시도 바람.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Message message = handler.obtainMessage();
                        message.arg1 = 2;
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

    public void executeTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (startTime != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startTime--;
                            timer.setText("남은시간: " + startTime + "초");
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        startTime = 0;
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final int grade = calculateGrade();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                updateQuizScore(grade);
                            }
                        }).start();
                    }
                });
            }
        }).start();
    }
}
