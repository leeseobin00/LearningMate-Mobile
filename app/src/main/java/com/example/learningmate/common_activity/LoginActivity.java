package com.example.learningmate.common_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learningmate.MainActivity;
import com.example.learningmate.R;
import com.example.learningmate.TestFileActivity;
import com.example.learningmate.model.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean result = msg.getData().getBoolean("login");
            if (result) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);    // 메인 화면으로 이동
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, TestFileActivity.class));
            }
        });
        et_id = findViewById(R.id.editTextIdLogin);
        et_pass = findViewById(R.id.editTextPwLogin);
        btn_login = findViewById(R.id.btnLogin);
        btn_register = findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);  // 회원가입 페이지로 이동
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                if (userID.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userPass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postUserInfo(userID, userPass);
                    }
                }).start();
            }
        });
    }

    public boolean postUserInfo(String id, String pw) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("signin_uid", id)
                    .add("signin_password", pw)
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
                    if (data.contains("Wrong Password!") || data.contains("Invalid User!")) {
                        Bundle bundle = new Bundle();
                        Message message = handler.obtainMessage();
                        bundle.putBoolean("login", false);
                        bundle.putString("message", data.equals("Wrong Password!") ? "비밀번호가 옳지 않습니다!" : "존재하지 않는 계정입니다!");
                        message.setData(bundle);
                        handler.sendMessage(message);
                        return false;
                    }
                    JSONObject jsonObject = new JSONObject(data);
                    User user = new User(
                            jsonObject.get("uid").toString(),
                            jsonObject.get("userName").toString(),
                            Integer.parseInt(jsonObject.get("identity").toString()),
                            jsonObject.get("pair_uid").toString(),
                            jsonObject.get("rDate").toString()
                    );
                    Bundle bundle = new Bundle();
                    Message message = handler.obtainMessage();

                    bundle.putString("result", data);
                    bundle.putBoolean("login", true);
                    bundle.putSerializable("user", user);

                    User.currentUser = user;
                    message.setData(bundle);
                    handler.sendMessage(message);
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