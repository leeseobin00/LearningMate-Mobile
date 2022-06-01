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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.learningmate.MainActivity;
import com.example.learningmate.R;
import com.example.learningmate.StartActivity;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_name, et_passck;
    private Button btn_register, btn_validate;
    private RadioButton btn_radio1, btn_radio2;
    private boolean validate = false;
    private boolean isCheckedDuplicate = false;
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return;
            }

            int chkResult = msg.arg1;
            if (chkResult == 1) {
                String message = "이미 존재하는 아이디입니다.";
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            } else if (chkResult == 0) {
                String message = "사용 가능한 아이디입니다.";
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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
        btn_register = findViewById(R.id.btnRegister2);
        btn_radio1 = findViewById(R.id.radioBtn1);
        btn_radio2 = findViewById(R.id.radioBtn2);
        btn_validate = findViewById(R.id.btnValidate);

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_id.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isCheckedDuplicate = true;
                        boolean result = getCheckDuplicateUid(et_id.getText().toString());
                    }
                }).start();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "이름을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_id.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_pass.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!btn_radio1.isChecked() && !btn_radio2.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "멘토 혹은 멘티를 선택해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isCheckedDuplicate) {
                    Toast.makeText(RegisterActivity.this, "아이디 중복 체크 바랍니다!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!et_pass.getText().toString().equals(et_passck.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 다릅니다!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (validate) {
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String identity = btn_radio1.isChecked() ? "1" : "0";
                        postRegister(et_id.getText().toString()
                                , et_pass.getText().toString()
                                , et_name.getText().toString()
                                , identity);
                    }
                }).start();

            }
        });
        findViewById(R.id.back_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
            }
        });
    }

    public void postRegister(String id, String pw, String userName, String identity) {
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
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

                }
            } else {
                Log.d("response", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
