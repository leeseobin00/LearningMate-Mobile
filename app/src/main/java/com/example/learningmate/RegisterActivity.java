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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_name, et_passck;
    private Button btn_register, btn_validate;
    private RadioGroup radioGroup;
    private RadioButton btn_radio1, btn_radio2, btn_radio3;
    private AlertDialog dialog;
    private boolean validate = false;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean register = msg.getData().getBoolean("register");
            if (register) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postLogin(et_id.getText().toString()
                                , et_pass.getText().toString());
                    }
                }).start();
                return;
            }

            boolean result = msg.getData().getBoolean("login");
            if (result) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);  // 회원가입 페이지로 이동
                startActivity(intent);
                return;
            }
            int chkResult = msg.arg1;

            if (chkResult == 1) {
                String message = "이미 존재하는 아이디입니다.";
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (chkResult == 0) {
                String message = "사용 가능한 아이디입니다.";
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.editTextName);
        et_id = findViewById(R.id.editTextId);
        et_pass = findViewById(R.id.editTextPw);
        et_passck = findViewById(R.id.editTextPwCheck);

        btn_validate = findViewById(R.id.btnValidate);

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean result = getCheckDuplicateUid(et_id.getText().toString());
                    }
                }).start();
            }
        });
        btn_register = findViewById(R.id.btnRegister2);

        radioGroup = findViewById(R.id.radioGroup);
        btn_radio1 = findViewById(R.id.radioBtn1);
        btn_radio2 = findViewById(R.id.radioBtn2);

        String id = et_id.getText().toString();
        String pw = et_pass.getText().toString();
        String name = et_name.getText().toString();

        if (btn_radio1.isChecked()) {
            ((MainActivity) MainActivity.mContext).postUserInfo(id, pw, name, "1");
        } else if (btn_radio2.isChecked()) {
            ((MainActivity) MainActivity.mContext).postUserInfo(id, pw, name, "0");
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String identity = btn_radio1.isChecked() ? "1" : "0";
                        if (et_pass.getText().toString().equals(et_passck.getText().toString())) {
                            postRegister(et_id.getText().toString()
                                    , et_pass.getText().toString()
                                    , et_name.getText().toString()
                                    , identity);

                        }
                    }
                }).start();

            }
        });
    }

    public boolean postRegister(String id, String pw, String userName, String identity) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("signup_uid", id)
                    .add("signup_password", pw)
                    .add("signup_userName", userName)
                    .add("signup_identity", identity)
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
                    if (data.equals("1")) {
                        Bundle bundle = new Bundle();
                        Message message = handler.obtainMessage();
                        bundle.putBoolean("register", true);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        return true;
                    } else {
                        return false;
                    }
//                    Log.d("body", data);
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

    public boolean getCheckDuplicateUid(String check) {
        try {
            String url = "http://windry.dothome.co.kr/se_learning_mate/controller/account_controller.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("check_id", check)
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
                    if (data.equals("1")) {
                        validate = true;
                        Message message = handler.obtainMessage();
                        message.arg1 = 1;
                        handler.sendMessage(message);
                        return true;
                    } else {
                        validate = false;
                        Message message = handler.obtainMessage();
                        message.arg1 = 0;
                        handler.sendMessage(message);
                        return false;
                    }
//                    Log.d("body", data);
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

    public void postLogin(String id, String pw) {
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
                        message.setData(bundle);
                        handler.sendMessage(message);
                        handler.sendMessage(message);
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

//                    Log.d("response", data);
//                    Log.d("uid",jsonObject.get("uid").toString());
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
